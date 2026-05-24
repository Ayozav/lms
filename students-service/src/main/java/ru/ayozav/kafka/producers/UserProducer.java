package ru.ayozav.kafka.producers;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ayozav.kafka.json.JsonSerializer;
import ru.ayozav.models.User;

import java.util.Properties;

public class UserProducer {

    public static final String GET_ONE_USER_TOPIC = "javalin-get-one-user";
    public static final String ADD_NEW_USER_TOPIC = "javalin-add-user";
    public static final String DELETE_USER_TOPIC = "javalin-del-user";
    public static final String UPDATE_USER_TOPIC = "javalin-update-user";

    private static final String JAVALIN_USER_PRODUCER_ID = "javalin-user-producer";

    private static final Logger log = LoggerFactory.getLogger(UserProducer.class);


    private final Producer<String, User> userProducer;
    private final String bootstrapServer;

    public UserProducer(String bootstrapServer) {
        this.bootstrapServer = bootstrapServer;
        this.userProducer = createProducer();
    }

    private Producer<String, User> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, JAVALIN_USER_PRODUCER_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());

        return new KafkaProducer<>(props);
    }

    public void produceGetOneUser(String key, User user) {
        ProducerRecord<String, User> userRecord = new ProducerRecord<>(GET_ONE_USER_TOPIC, key, user);
        userProducer.send(userRecord);
    }

    public void produceAddUser(String key, User user) {
        ProducerRecord<String, User> userRecord = new ProducerRecord<>(ADD_NEW_USER_TOPIC, key, user);
        userProducer.send(userRecord, (metadata, exception) -> {
            if (exception != null) {
                log.error("Failed to send ADD_USER message, key={}", key, exception);
            } else {
                log.info("ADD_USER sent: topic={}, offset={}, partition={}",
                        metadata.topic(), metadata.offset(), metadata.partition());
            }
        });

    }

    public void produceDeleteUser(String key, User user) {
        ProducerRecord<String, User> userRecord = new ProducerRecord<>(DELETE_USER_TOPIC, key, user);
        userProducer.send(userRecord);
        log.info("USER DELETE RECORD CREATED");
    }

    public void produceUpdateUser(String key, User user) {
        ProducerRecord<String, User> userRecord = new ProducerRecord<>(UPDATE_USER_TOPIC, key, user);
        userProducer.send(userRecord);
        log.info("USER UPDATE RECORD CREATED");
    }
}
