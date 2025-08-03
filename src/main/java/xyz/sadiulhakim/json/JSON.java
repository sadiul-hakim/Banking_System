package xyz.sadiulhakim.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JSON {
    private JSON() {
    }

    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    public static String stringify(Object data) throws JsonProcessingException {
        return MAPPER.writeValueAsString(data);
    }

    public static <T> T parse(String data, Class<T> type) throws JsonProcessingException {
        return MAPPER.readValue(data, type);
    }
}
