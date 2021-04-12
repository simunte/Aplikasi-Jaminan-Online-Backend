package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ViewBankGuaranteeHistoryDTO {
    private Long id;

    @JsonProperty(value = "date_modified")
    private Long modificationDate;

    @JsonProperty(value = "modified_by")
    private String modifiedBy;

    @JsonProperty(value = "date_created")
    private Long creationDate;

    @JsonProperty(value = "created_by")
    private String createdBy;

    @JsonProperty(value = "nomor_jaminan")
    private String nomorJaminan;

    @JsonProperty(value = "bank_guarantee_status")
    private String bgStatus;

    private String notes;
}
