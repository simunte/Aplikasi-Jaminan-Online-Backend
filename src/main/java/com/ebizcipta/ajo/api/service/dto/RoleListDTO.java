package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RoleListDTO extends RoleDTO {
    @JsonProperty(value = "menus")
    private List<MenuListDTO> menuList;
}
