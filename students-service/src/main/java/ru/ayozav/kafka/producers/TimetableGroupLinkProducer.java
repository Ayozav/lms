package ru.ayozav.kafka.producers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ayozav.kafka.json.JsonSerializer;
import ru.ayozav.models.TimetableGroupLink;

import java.util.Properties;

public class TimetableGroupLinkProducer {

    public static final String ADD_NEW_TOPIC = "javalin-add-timetable-group-link";
    public static final String DELETE_TOPIC = "javalin-del-timetable-group-link";

    private static final String PRODUCER_ID = "javalin-timetable-group-link-producer";

    private static final Logger log = LoggerFactory.getLogger(TimetableGroupLinkProducer.class);

    private final Producer<String, TimetableGroupLink> producer;
    private final String bootstrapServer;

    public TimetableGroupLinkProducer(String bootstrapServer) {
        this.bootstrapServer = bootstrapServer;
        this.producer = createProducer();
    }

    private Producer<String, TimetableGroupLink> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, PRODUCER_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());

        return new KafkaProducer<>(props);
    }

    public void produceAdd(String key, TimetableGroupLink link) {
        ProducerRecord<String, TimetableGroupLink> record = new ProducerRecord<>(ADD_NEW_TOPIC, key, link);
        producer.send(record);
        log.info("ADD RECORD was sent: topic={}, key={}", record.topic(), record.key());
    }

    public void produceDelete(String key, TimetableGroupLink link) {
        ProducerRecord<String, TimetableGroupLink> record = new ProducerRecord<>(DELETE_TOPIC, key, link);
        producer.send(record);
        log.info("DELETE RECORD was sent: topic={}, key={}", record.topic(), record.key());
    }
}