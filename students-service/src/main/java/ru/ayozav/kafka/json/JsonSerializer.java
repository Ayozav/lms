package ru.ayozav.kafka.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

public class JsonSerializer<T> implements Serializer<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonSerializer() {
        // Регистрируем модуль для работы с Java 8 Date/Time
        objectMapper.registerModule(new JavaTimeModule());
        // Опционально: отключаем сериализацию дат как timestamp'ов (будет строка в формате ISO)
        // objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }


    @Override
    public byte[] serialize(String s, T object) {
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new SerializationException(e);
        }
    }
}