package ru.ayozav.kafka.consumers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jetbrains.annotations.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.repositories.HomeworksEventRepository;
import ru.ayozav.kafka.json.JsonDeserializer;
import ru.ayozav.kafka.producers.HomeworkProducer;
import ru.ayozav.models.Homework;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Консьюмер Kafka для обработки событий домашних заданий (добавление, удаление, обновление).
 */
public class HomeworkConsumer implements AutoCloseable {
    private static final Logger log = LogManager.getLogger(HomeworkConsumer.class);

    private final HomeworksEventRepository eventRepository;

    private static final String GROUP_ID = "javalin-homework-consumer";
    private static final Duration POLL_TIMEOUT = Duration.ofMillis(1000);
    private static final List<String> SUBSCRIBED_TOPICS = Arrays.asList(
            HomeworkProducer.ADD_NEW_TOPIC,
            HomeworkProducer.DELETE_TOPIC,
            HomeworkProducer.UPDATE_TOPIC
    );

    private final KafkaConsumer<String, Homework> consumer;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean running = false;

    /**
     * Создаёт консьюмер для указанных серверов Kafka.
     *
     * @param bootstrapServers адреса брокеров Kafka (например, "localhost:9092")
     * @param factory          фабрика соединений с БД
     */
    public HomeworkConsumer(String bootstrapServers, HikariConnectionFactory factory) {
        this.eventRepository = new HomeworksEventRepository(factory);
        this.consumer = new KafkaConsumer<>(createConsumerConfig(bootstrapServers));
    }

    /**
     * Запускает асинхронное потребление сообщений.
     */
    public void start() {
        if (running) {
            log.warn("HomeworkConsumer is already running");
            return;
        }

        running = true;
        executor.submit(this::consumeMessages);
        log.info("HomeworkConsumer started, subscribed to topics: {}", SUBSCRIBED_TOPICS);
    }

    /**
     * Останавливает потребление сообщений и корректно закрывает консьюмер.
     */
    @Override
    public void close() {
        if (!running) {
            return;
        }

        log.info("Stopping HomeworkConsumer...");
        running = false;
        consumer.wakeup();
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted while waiting for consumer executor to terminate", e);
        }
        log.info("HomeworkConsumer stopped");
    }

    private void consumeMessages() {
        consumer.subscribe(SUBSCRIBED_TOPICS);

        try {
            while (running) {
                ConsumerRecords<String, Homework> records = consumer.poll(POLL_TIMEOUT);
                if (records.isEmpty()) {
                    continue;
                }

                log.info("Polled {} records from Kafka", records.count());
                for (ConsumerRecord<String, Homework> record : records) {
                    processRecord(record);
                }
            }
        } catch (WakeupException e) {
            // Игнорируем — это нормальный способ прервать poll()
        } catch (Exception e) {
            log.error("Unexpected error in consumer loop", e);
        } finally {
            consumer.close();
        }
    }

    private void processRecord(ConsumerRecord<String, Homework> record) {
        try {
            String topic = record.topic();
            String key = record.key();
            Homework homework = record.value();

            log.info("Processing message: topic={}, key={}, homework={}", topic, key, homework);
            switch (topic) {
                case HomeworkProducer.ADD_NEW_TOPIC:
                    eventRepository.add(
                            homework.getLessonId(),
                            homework.getSemesterId(),
                            homework.getDeadline(),
                            homework.getDescription(),
                            homework.getFileLink()
                    );
                    break;
                case HomeworkProducer.DELETE_TOPIC:
                    eventRepository.deleteById(homework.getId());
                    break;
                case HomeworkProducer.UPDATE_TOPIC:
                    eventRepository.update(
                            homework.getId(),
                            homework.getLessonId(),
                            homework.getSemesterId(),
                            homework.getDeadline(),
                            homework.getDescription(),
                            homework.getFileLink()
                    );
                    break;
                default:
                    log.warn("Unknown topic: {}", topic);
            }
        } catch (Exception e) {
            log.error("Failed to process record from topic: {}, key: {}",
                    record.topic(), record.key(), e);
        }
    }

    @NotNull
    private Properties createConsumerConfig(String bootstrapServers) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        props.put("json.deserializer.target.type", Homework.class.getName());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // Улучшенная устойчивость к временным сбоям
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "10000");
        return props;
    }
}