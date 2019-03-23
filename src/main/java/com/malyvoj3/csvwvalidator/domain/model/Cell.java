package com.malyvoj3.csvwvalidator.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class Cell {

    private String aboutUrl;
    private Column column;
    private List<CellError> errors = new ArrayList<>();
    private boolean ordered;
    private String propertyUrl;
    private Row row;
    private String stringValue;
    private Table table;
    // TODO opravdu object?
    private Object value;
    private String valueUrl;

}
