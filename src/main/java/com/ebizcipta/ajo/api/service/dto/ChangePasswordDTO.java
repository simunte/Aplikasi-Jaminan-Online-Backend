package com.ebizcipta.ajo.api.service.dto;

import lombok.Data;

@Data
public class ChangePasswordDTO  {
    private Long userId;
    private String newPassword;
    private String reType;

}
