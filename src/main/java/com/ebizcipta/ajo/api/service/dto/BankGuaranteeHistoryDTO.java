package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

@Data
public class BankGuaranteeHistoryDTO {
    private Long id;

    @JsonProperty(value = "id_jaminan")
    private Long idJaminan;

    @JsonProperty(value = "bank_guarantee_status")
    private String bgStatus;

    private String notes;
}
