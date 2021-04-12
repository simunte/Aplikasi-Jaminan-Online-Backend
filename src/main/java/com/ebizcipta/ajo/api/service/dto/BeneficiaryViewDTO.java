package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BeneficiaryViewDTO extends BaseDTO{
    private Long id;
    @JsonProperty(value = "nama_beneficiary")
    private String namaBeneficiary;
    @JsonProperty(value = "code_beneficiary")
    private String codeBeneficiary;
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
    private String status;
    private String description;
    private Boolean activated;
    private String notes;
}
