package com.ebizcipta.ajo.api.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ErrorDetails {
    private Long timestamp;
    private String message;

    @JsonProperty("http_method")
    private String httpMethod;

    private String uri;
    private List<Violation> violations = new ArrayList<>();
}
