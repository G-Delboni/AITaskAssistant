package com.app.aiassistant.ai;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;

public class NullStringLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(
            JsonParser parser,
            DeserializationContext context) throws IOException {

        String value = parser.getValueAsString();

        if (value == null || value.isBlank() || value.equalsIgnoreCase("null")) {
            return null;
        }

        return LocalDateTime.parse(value);
    }
}
