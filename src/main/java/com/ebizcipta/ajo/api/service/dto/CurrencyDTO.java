package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CurrencyDTO {
    private Long id;
    @JsonProperty(value = "currency")
    private String currency;
    private String status;
    private Boolean activated;
    private String description;
}
