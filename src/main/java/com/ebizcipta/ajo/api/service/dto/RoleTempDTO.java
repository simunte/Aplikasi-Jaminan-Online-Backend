package com.ebizcipta.ajo.api.service.dto;

import lombok.Data;

@Data
public class RoleTempDTO {
    private Integer roleId;
    private String data;
    private Boolean isReject;
    private String note;
    private String name;
}
