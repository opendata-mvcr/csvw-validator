package com.malyvoj3.csvwvalidator.metadata.parser.properties.dialect;

import com.fasterxml.jackson.databind.JsonNode;
import com.malyvoj3.csvwvalidator.metadata.domain.DialectDescription;
import com.malyvoj3.csvwvalidator.metadata.domain.properties.BooleanAtomicProperty;
import com.malyvoj3.csvwvalidator.metadata.parser.properties.PropertyParser;

public class DoubleQuotePropertyParser<T extends DialectDescription> implements PropertyParser<T> {

    private static final boolean DOUBLE_QUOTE_DEFAULT_VALUE = true;

    @Override
    public T parseProperty(T description, JsonNode property) {
        BooleanAtomicProperty doubleQuote;
        if (property.isBoolean()) {
            doubleQuote = new BooleanAtomicProperty(property, property.booleanValue());
        } else {
            doubleQuote = new BooleanAtomicProperty(property, DOUBLE_QUOTE_DEFAULT_VALUE);
        }
        description.setDoubleQuote(doubleQuote);
        return description;
    }
}
