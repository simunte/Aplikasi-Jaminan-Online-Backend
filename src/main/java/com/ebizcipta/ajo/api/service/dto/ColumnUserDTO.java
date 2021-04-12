package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Arrays;

@Data
public class ColumnUserDTO {
    @JsonProperty(value = "list_of_column")
    private String listColumn;

    @JsonProperty(value = "username")
    private String username;
}
