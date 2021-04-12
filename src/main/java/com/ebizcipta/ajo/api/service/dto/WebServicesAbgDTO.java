package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Lob;

@Data
public class WebServicesAbgDTO {
    private Long id;
    @JsonProperty(value = "beneficiay_id")
    private Long beneficiaryId;
    @JsonProperty(value = "auth_token_url")
    private String authTokenUrl;
    @JsonProperty(value = "input_jaminan_url")
    private String inputJaminanUrl;
    @JsonProperty(value = "validity_jaminan_url")
    private String validityJaminanUrl;
    @JsonProperty(value = "grant_type")
    private String grantType;
    @JsonProperty(value = "username")
    private String username;
    @JsonProperty(value = "password")
    private String password;
    @JsonProperty(value = "client_id")
    private String clientId;
    @JsonProperty(value = "client_secret")
    private String clientSecret;
    private String description;
}