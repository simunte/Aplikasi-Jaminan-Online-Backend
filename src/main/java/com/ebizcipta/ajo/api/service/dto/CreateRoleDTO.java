package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateRoleDTO extends RoleDTO {
    @JsonProperty(value = "menus")
    private List<PrivelegeDTO> privilegeList;
//    private Long beneficiaryId;
}
