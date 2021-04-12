package com.ebizcipta.ajo.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "beneficiary")
public class Beneficiary extends Base{

    @Column(name = "nama_beneficiary")
    private String namaBeneficiary;

    @Column(name = "code_beneficiary")
    private String codeBeneficiary;

    @Column(name = "auth_token_url")
    private String authTokenUrl;
    @Column(name = "input_jaminan_url")
    private String inputJaminanUrl;
    @Column(name = "validity_jaminan_url")
    private String validityJaminanUrl;
    @Column(name = "grant_type")
    private String grantType;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "client_id")
    private String clientId;
    @Column(name = "client_secret")
    private String clientSecret;

    private String status;

    private String description;

    private Boolean activated;

    private String notes;
}
