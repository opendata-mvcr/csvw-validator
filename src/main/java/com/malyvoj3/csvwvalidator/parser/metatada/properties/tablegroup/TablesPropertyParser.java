package com.malyvoj3.csvwvalidator.parser.metatada.properties.tablegroup;

import com.fasterxml.jackson.databind.JsonNode;
import com.malyvoj3.csvwvalidator.domain.metadata.TableGroupDescription;
import com.malyvoj3.csvwvalidator.parser.metatada.properties.PropertyParser;

public class TablesPropertyParser<T extends TableGroupDescription> implements PropertyParser<T> {

    @Override
    public T parseProperty(T description, JsonNode property) {
        return null;
    }

}
