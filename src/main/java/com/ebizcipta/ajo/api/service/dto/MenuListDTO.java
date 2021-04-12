package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MenuListDTO {
    @JsonProperty(value = "menu_id")
    private Long id;

    private String name;

    private String description;

    @JsonProperty(value = "head_of_menu")
    private String headMenu;

    @JsonProperty(value = "alias_menu")
    private String aliasMenu;

    @JsonProperty(value = "create_access")
    private Boolean create;

    @JsonProperty(value = "read_access")
    private Boolean read;

    @JsonProperty(value = "update_access")
    private Boolean update;

    @JsonProperty(value = "delete_access")
    private Boolean delete;

    @JsonProperty(value = "approval_access")
    private Boolean approval;
}
