package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MenuBaseDTO {
    private Long id;
    private String name;
    @JsonProperty(value = "head_of_menu")
    private String headMenu;
    private String description;
    @JsonProperty(value = "alias_menu")
    private String aliasMenu;
}
