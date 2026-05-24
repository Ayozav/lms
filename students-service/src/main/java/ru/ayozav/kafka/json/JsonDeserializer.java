package ru.ayozav.kafka.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class JsonDeserializer<T> implements Deserializer<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Class<T> targetClass;

    // Конструктор без аргументов — обязателен для Kafka
    public JsonDeserializer() {
        this.objectMapper.registerModule(new JavaTimeModule());
        // Отключаем сериализацию дат как timestamp'ов — будет формат ISO
        // this.objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Получаем имя класса из конфигурации
        String className = (String) configs.get("json.deserializer.target.type");
        if (className == null) {
            throw new ConfigException("Configuration 'json.deserializer.target.type' is required for JsonDeserializer");
        }
        try {
            this.targetClass = (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ConfigException("Failed to load target class: " + className);
        }
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            return objectMapper.readValue(data, targetClass);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new SerializationException("Error deserializing JSON message", e);
        }
    }

    @Override
    public void close() {
        // Освобождение ресурсов при необходимости
    }
}
