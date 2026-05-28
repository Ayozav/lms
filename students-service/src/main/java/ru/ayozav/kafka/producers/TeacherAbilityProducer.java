package ru.ayozav.kafka.producers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.ayozav.kafka.json.JsonSerializer;
import ru.ayozav.models.TeachersAbility;

import java.util.Properties;

public class TeacherAbilityProducer {

    public static final String ADD_NEW_TOPIC = "javalin-add-teachers-ability";
    public static final String DELETE_TOPIC = "javalin-del-teachers-ability";

    private static final String PRODUCER_ID = "javalin-teachers-ability-producer";

    private static final Logger log = LogManager.getLogger(TeacherAbilityProducer.class);

    private final Producer<String, TeachersAbility> producer;
    private final String bootstrapServer;

    public TeacherAbilityProducer(String bootstrapServer) {
        this.bootstrapServer = bootstrapServer;
        this.producer = createProducer();
    }

    private Producer<String, TeachersAbility> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, PRODUCER_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());

        return new KafkaProducer<>(props);
    }

    public void produceAdd(String key, TeachersAbility ability) {
        ProducerRecord<String, TeachersAbility> record = new ProducerRecord<>(ADD_NEW_TOPIC, key, ability);
        producer.send(record);
        log.info("ADD RECORD was sent: topic={}, key={}", record.topic(), record.key());
    }

    public void produceDelete(String key, TeachersAbility ability) {
        ProducerRecord<String, TeachersAbility> record = new ProducerRecord<>(DELETE_TOPIC, key, ability);
        producer.send(record);
        log.info("DELETE RECORD was sent: topic={}, key={}", record.topic(), record.key());
    }
}