package com.ebizcipta.ajo.api.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Violation {
    @JsonProperty("field_name")
    private final String fieldName;
    private final String message;
}
