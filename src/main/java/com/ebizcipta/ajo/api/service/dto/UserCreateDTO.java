package com.ebizcipta.ajo.api.service.dto;

import com.ebizcipta.ajo.api.util.Constants;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

@Data
public class UserCreateDTO {
    private Long id;

    private String username;

    private String email;

    private String password;

    @JsonProperty(value = "first_name")
    private String firstName;

    @JsonProperty(value = "last_name")
    private String lastName;

    private String position;

    private String note;

    private String signature;

    private String status;

    @JsonProperty(value = "modified_by")
    private String modifiedBy;

    private boolean isEnabled;

    private List<RoleDTO> roles;

    private Long beneficiaryId;

    private Boolean isFirstLogin;

    private Boolean isExternal;

    private Boolean isForceChangePassword;

    @JsonProperty(value = "nama_beneficiary")
    private String beneficiaryName;

    @JsonProperty(value = "create_date")
    private String createDate;

    @JsonProperty(value = "update_manual_by")
    private String updateManualBy;

    private String approvedBy;


}
