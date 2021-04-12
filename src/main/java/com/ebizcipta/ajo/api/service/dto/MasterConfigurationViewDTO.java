package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MasterConfigurationViewDTO extends BaseDTO{
    private Long id;

    @JsonProperty(value = "username")
    private String username;

    @JsonProperty(value = "password")
    private String password;

    @JsonProperty(value = "smtp_hostname")
    private String smtpHostname;

    @JsonProperty(value = "smtp_password")
    private String smtpPassword;

    @JsonProperty(value = "smtp_port")
    private String smtpPort;

    @JsonProperty(value = "retention_data")
    private String retentionData;

    @JsonProperty(value = "mailing_list")
    private String mailingList;

    private Boolean activated;

    private String notes;

    private String status;
}
