package com.malyvoj3.csvwvalidator.parser.metatada.properties.inherited;

import com.fasterxml.jackson.databind.JsonNode;
import com.malyvoj3.csvwvalidator.domain.metadata.InheritanceDescription;
import com.malyvoj3.csvwvalidator.parser.metatada.properties.PropertyParser;

public class LangPropertyParser<T extends InheritanceDescription> implements PropertyParser<T> {

    @Override
    public T parseProperty(T description, JsonNode property) {
//
//        UriTemplateProperty aboutUrl = new UriTemplateProperty();
//        description.setAboutUrl();
        return description;
    }
}
