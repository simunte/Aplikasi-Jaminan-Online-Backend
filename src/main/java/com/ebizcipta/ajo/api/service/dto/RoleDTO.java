package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.time.Instant;

@Data
public class RoleDTO {

    private Long id;

    private String name;

    private String description;

    private Boolean activated;

    private String status;

    private String code;

    private Long modifiedDate;

    private String modifiedBy;

    private String note;

}
