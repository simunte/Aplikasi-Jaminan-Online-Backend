package com.ebizcipta.ajo.api.service.dto;

import lombok.Data;

@Data
public class GlobalApprovalDTO {
    private Long id;
    private Boolean approval;
    private String notes;
}
