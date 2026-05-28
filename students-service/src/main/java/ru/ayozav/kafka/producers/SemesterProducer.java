package ru.ayozav.kafka.producers;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.ayozav.kafka.json.JsonSerializer;
import ru.ayozav.models.Semester;

import java.util.Properties;

public class SemesterProducer {

    public static final String ADD_NEW_TOPIC = "javalin-add-semester";
    public static final String DELETE_TOPIC = "javalin-del-semester";
    public static final String UPDATE_TOPIC = "javalin-update-semester";

    private static final String PRODUCER_ID = "javalin-semester-producer";

    private static final Logger log = LogManager.getLogger(SemesterProducer.class);


    private final Producer<String, Semester> producer;
    private final String bootstrapServer;

    public SemesterProducer(String bootstrapServer) {
        this.bootstrapServer = bootstrapServer;
        this.producer = createProducer();
    }

    private Producer<String, Semester> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, PRODUCER_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());

        return new KafkaProducer<>(props);
    }

    public void produceAdd(String key, Semester semester) {
        ProducerRecord<String, Semester> record = new ProducerRecord<>(ADD_NEW_TOPIC, key, semester);
        producer.send(record);
        log.info("ADD RECORD was sent: topic={}, key={}", record.topic(), record.key());
    }

    public void produceDelete(String key, Semester semester) {
        ProducerRecord<String, Semester> record = new ProducerRecord<>(DELETE_TOPIC, key, semester);
        producer.send(record);
        log.info("DELETE RECORD was sent: topic={}, key={}", record.topic(), record.key());

    }

    public void produceUpdate(String key, Semester semester) {
        ProducerRecord<String, Semester> record = new ProducerRecord<>(UPDATE_TOPIC, key, semester);
        producer.send(record);
        log.info("UPDATE RECORD was sent: topic={}, key={}", record.topic(), record.key());
    }
}
