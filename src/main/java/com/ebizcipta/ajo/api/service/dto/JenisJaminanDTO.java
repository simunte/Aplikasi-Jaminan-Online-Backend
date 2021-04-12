package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JenisJaminanDTO {
    private Long id;
    @JsonProperty(value = "jenis_jaminan")
    private String jenisJaminan;
    @JsonProperty(value = "code_jaminan")
    private String codeJaminan;
    private String status;
    private String description;
    private Boolean activated;
}
