package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Lob;

@Data
public class AlamatBankPenerbitViewDTO extends BaseDTO{
    private Long id;
    @JsonProperty(value = "alamat_bank_penerbit")
    private String alamatBankPenerbit;
    private String status;
    private String code;
    private String cabang;
    private String description;
    private Boolean activated;
}
