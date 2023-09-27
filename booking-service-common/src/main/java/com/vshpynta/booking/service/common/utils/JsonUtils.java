package com.vshpynta.booking.service.common.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import static java.util.Objects.isNull;

@UtilityClass
public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .registerModule(new JavaTimeModule());

    private static final ObjectMapper objectMapperIndent = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .registerModule(new JavaTimeModule());

    @SneakyThrows
    public static <T> T deserialize(String json, Class<T> tClass) {
        return isNull(json) ? null : objectMapper.readValue(json, tClass);
    }

    @SneakyThrows
    public static <T> T deserialize(String json, TypeReference<T> typeReference) {
        return isNull(json) ? null : objectMapper.readValue(json, typeReference);
    }

    @SneakyThrows
    public static <T> T treeToValue(TreeNode treeNode, Class<T> tClass) {
        return isNull(treeNode) ? null : objectMapper.treeToValue(treeNode, tClass);
    }

    @SneakyThrows
    public static <T> String serialize(T object) {
        return isNull(object) ? null : objectMapper.writeValueAsString(object);
    }

    @SneakyThrows
    public static <T> String serializeIndent(T object) {
        return isNull(object) ? null : objectMapperIndent.writeValueAsString(object);
    }

    @SneakyThrows
    public static <T> String toJson(T toMarshall) {
        return serialize(toMarshall);
    }
}
