package org.example.cardrangefinder.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.example.cardrangefinder.entity.CardRange;

import java.io.IOException;
import java.math.BigDecimal;

public class CardRangeDeserializer extends JsonDeserializer<CardRange> {

    @Override
    public CardRange deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        if (node.get("bin") == null || node.get("min_range") == null || node.get("max_range") == null
                || node.get("alpha_code") == null || node.get("bank_name") == null) {
            return null;
        }

        int bin = Integer.parseInt(node.get("bin").asText());
        BigDecimal minRange = parseBigDecimal(node, "min_range");
        BigDecimal maxRange = parseBigDecimal(node, "max_range");

        String alphaCode = parseString(node, "alpha_code");
        String bankName = parseString(node, "bank_name");

        if (minRange == null || maxRange == null || alphaCode == null || bankName == null) {
            return null;
        }

        return new CardRange(bin, minRange, maxRange, alphaCode, bankName);
    }

    private BigDecimal parseBigDecimal(JsonNode node, String fieldName) {
        String value = node.get(fieldName).asText();
        return (value == null || value.equals("null") || value.trim().isEmpty()) ? null : new BigDecimal(value);
    }

    private String parseString(JsonNode node, String fieldName) {
        String value = node.get(fieldName).asText();
        return (value == null || value.equals("null") || value.trim().isEmpty()) ? null : value;
    }
}
