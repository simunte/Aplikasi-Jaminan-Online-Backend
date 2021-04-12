package com.ebizcipta.ajo.api.service.dto;

import lombok.Data;

@Data
public class ApprovalUserDTO {
    public String action;
    public Long userId;
    public String note;
}
