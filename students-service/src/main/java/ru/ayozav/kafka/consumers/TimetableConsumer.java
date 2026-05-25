package ru.ayozav.kafka.consumers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.repositories.TimetablesEventRepository;
import ru.ayozav.kafka.json.JsonDeserializer;
import ru.ayozav.kafka.producers.TimetableProducer;
import ru.ayozav.models.Timetable;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Консьюмер Kafka для обработки событий расписания (добавление, удаление, обновление).
 */
public class TimetableConsumer implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(TimetableConsumer.class);

    private final TimetablesEventRepository eventRepository;

    private static final String GROUP_ID = "javalin-timetable-consumer";
    private static final Duration POLL_TIMEOUT = Duration.ofMillis(1000);
    private static final List<String> SUBSCRIBED_TOPICS = Arrays.asList(
            TimetableProducer.ADD_NEW_TOPIC,
            TimetableProducer.DELETE_TOPIC,
            TimetableProducer.UPDATE_TOPIC
    );

    private final KafkaConsumer<String, Timetable> consumer;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean running = false;

    /**
     * Создаёт консьюмер для указанных серверов Kafka.
     *
     * @param bootstrapServers адреса брокеров Kafka (например, "localhost:9092")
     * @param factory          фабрика соединений с БД
     */
    public TimetableConsumer(String bootstrapServers, HikariConnectionFactory factory) {
        this.eventRepository = new TimetablesEventRepository(factory);
        this.consumer = new KafkaConsumer<>(createConsumerConfig(bootstrapServers));
    }

    /**
     * Запускает асинхронное потребление сообщений.
     */
    public void start() {
        if (running) {
            log.warn("TimetableConsumer is already running");
            return;
        }

        running = true;
        executor.submit(this::consumeMessages);
        log.info("TimetableConsumer started, subscribed to topics: {}", SUBSCRIBED_TOPICS);
    }

    /**
     * Останавливает потребление сообщений и корректно закрывает консьюмер.
     */
    @Override
    public void close() {
        if (!running) {
            return;
        }

        log.info("Stopping TimetableConsumer...");
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
        log.info("TimetableConsumer stopped");
    }

    private void consumeMessages() {
        consumer.subscribe(SUBSCRIBED_TOPICS);

        try {
            while (running) {
                ConsumerRecords<String, Timetable> records = consumer.poll(POLL_TIMEOUT);
                if (records.isEmpty()) {
                    continue;
                }

                log.info("Polled {} records from Kafka", records.count());
                for (ConsumerRecord<String, Timetable> record : records) {
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

    private void processRecord(ConsumerRecord<String, Timetable> record) {
        try {
            String topic = record.topic();
            String key = record.key();
            Timetable timetable = record.value();

            log.info("Processing message: topic={}, key={}, timetable={}", topic, key, timetable);
            switch (topic) {
                case TimetableProducer.ADD_NEW_TOPIC:
                    eventRepository.add(
                            timetable.getSemesterId(),
                            timetable.getDisciplineId(),
                            timetable.getTeacherId(),
                            timetable.getDayOfWeek(),
                            timetable.getWeekParity(),
                            timetable.getRoom(),
                            timetable.getStartTime(),
                            timetable.getEndTime()
                    );
                    break;
                case TimetableProducer.DELETE_TOPIC:
                    eventRepository.deleteById(timetable.getId());
                    break;
                case TimetableProducer.UPDATE_TOPIC:
                    eventRepository.update(
                            timetable.getId(),
                            timetable.getSemesterId(),
                            timetable.getDisciplineId(),
                            timetable.getTeacherId(),
                            timetable.getDayOfWeek(),
                            timetable.getWeekParity(),
                            timetable.getRoom(),
                            timetable.getStartTime(),
                            timetable.getEndTime()
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
        props.put("json.deserializer.target.type", Timetable.class.getName());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // Улучшенная устойчивость к временным сбоям
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "10000");
        return props;
    }
}