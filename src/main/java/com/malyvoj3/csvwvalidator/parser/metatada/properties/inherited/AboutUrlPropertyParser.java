package com.malyvoj3.csvwvalidator.parser.metatada.properties.inherited;

import com.fasterxml.jackson.databind.JsonNode;
import com.malyvoj3.csvwvalidator.domain.metadata.InheritanceDescription;
import com.malyvoj3.csvwvalidator.domain.metadata.properties.UriTemplateProperty;
import com.malyvoj3.csvwvalidator.parser.metatada.properties.PropertyParser;

public class AboutUrlPropertyParser<T extends InheritanceDescription> implements PropertyParser<T> {

    private static final String ABOUT_URL_DEFAULT_VALUE = "";

    @Override
    public T parseProperty(T description, JsonNode property) {
        UriTemplateProperty aboutUrl;
        if (property.isTextual()) {
            // String value asi neni potreba?
            aboutUrl = new UriTemplateProperty(property, property.textValue());
        } else {
            aboutUrl = new UriTemplateProperty(property, ABOUT_URL_DEFAULT_VALUE);
        }
        description.setAboutUrl(aboutUrl);
        return description;
    }

}
