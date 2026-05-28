package ru.ayozav.kafka.producers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.ayozav.kafka.json.JsonSerializer;
import ru.ayozav.models.Grade;
import ru.ayozav.models.User;

import java.util.Properties;

public class GradeProducer {

    public static final String ADD_NEW_TOPIC = "javalin-add-grade";
    public static final String DELETE_TOPIC = "javalin-del-grade";
    public static final String UPDATE_TOPIC = "javalin-update-grade";

    private static final String PRODUCER_ID = "javalin-grade-producer";

    private static final Logger log = LogManager.getLogger(GradeProducer.class);


    private final Producer<String, Grade> producer;
    private final String bootstrapServer;

    public GradeProducer(String bootstrapServer) {
        this.bootstrapServer = bootstrapServer;
        this.producer = createProducer();
    }

    private Producer<String, Grade> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, PRODUCER_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());

        return new KafkaProducer<>(props);
    }


    public void produceAdd(String key, Grade grade) {
        ProducerRecord<String, Grade> record = new ProducerRecord<>(ADD_NEW_TOPIC, key, grade);
        producer.send(record);
        log.info("ADD RECORD was sent: topic={}, key={}", record.topic(), record.key());

    }

    public void produceDelete(String key, Grade grade) {
        ProducerRecord<String, Grade> record = new ProducerRecord<>(DELETE_TOPIC, key, grade);
        producer.send(record);
        log.info("DELETE RECORD was sent: topic={}, key={}", record.topic(), record.key());
    }

    public void produceUpdate(String key, Grade grade) {
        ProducerRecord<String, Grade> record = new ProducerRecord<>(UPDATE_TOPIC, key, grade);
        producer.send(record);
        log.info("UPDATE RECORD was sent: topic={}, key={}", record.topic(), record.key());
    }
}
