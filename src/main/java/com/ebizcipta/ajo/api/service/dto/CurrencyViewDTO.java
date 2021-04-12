package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CurrencyViewDTO extends BaseDTO{
    private Long id;
    @JsonProperty(value = "currency")
    private String currency;
    private String status;
    private Boolean activated;
    private String description;
    private String notes;
}
