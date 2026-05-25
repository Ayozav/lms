package ru.ayozav.kafka.producers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ayozav.kafka.json.JsonSerializer;
import ru.ayozav.models.Comment;

import java.util.Properties;

public class CommentProducer {

    public static final String ADD_NEW_TOPIC = "javalin-add-comment";
    public static final String DELETE_TOPIC = "javalin-del-comment";
    public static final String UPDATE_TOPIC = "javalin-update-comment";

    private static final String PRODUCER_ID = "javalin-comment-producer";

    private static final Logger log = LoggerFactory.getLogger(CommentProducer.class);

    private final Producer<String, Comment> producer;
    private final String bootstrapServer;

    public CommentProducer(String bootstrapServer) {
        this.bootstrapServer = bootstrapServer;
        this.producer = createProducer();
    }

    private Producer<String, Comment> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, PRODUCER_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());

        return new KafkaProducer<>(props);
    }

    public void produceAdd(String key, Comment comment) {
        ProducerRecord<String, Comment> record = new ProducerRecord<>(ADD_NEW_TOPIC, key, comment);
        producer.send(record);
        log.info("ADD RECORD was sent: topic={}, key={}", record.topic(), record.key());
    }

    public void produceDelete(String key, Comment comment) {
        ProducerRecord<String, Comment> record = new ProducerRecord<>(DELETE_TOPIC, key, comment);
        producer.send(record);
        log.info("DELETE RECORD was sent: topic={}, key={}", record.topic(), record.key());
    }

    public void produceUpdate(String key, Comment comment) {
        ProducerRecord<String, Comment> record = new ProducerRecord<>(UPDATE_TOPIC, key, comment);
        producer.send(record);
        log.info("UPDATE RECORD was sent: topic={}, key={}", record.topic(), record.key());
    }
}
