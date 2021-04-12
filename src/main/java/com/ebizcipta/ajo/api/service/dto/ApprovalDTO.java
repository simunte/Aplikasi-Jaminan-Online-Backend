package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ApprovalDTO {
    @JsonProperty(value = "id_jaminan")
    private Long idJaminan;
    @JsonProperty(value = "nomor_jaminan")
    private String nomorJaminan;
    private String action;
    private String notes;
}
