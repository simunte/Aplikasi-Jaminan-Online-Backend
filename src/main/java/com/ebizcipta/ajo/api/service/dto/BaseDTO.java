package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BaseDTO {
    @JsonProperty(value = "date_created")
    private Long creationDate;

    @JsonProperty(value = "created_by")
    private String createdBy;

    @JsonProperty(value = "date_modified")
    private Long modificationDate;

    @JsonProperty(value = "modified_by")
    private String modifiedBy;

    @JsonProperty(value = "date_approved")
    private Long approvedDate;

    @JsonProperty(value = "approved_by")
    private String approvedBy;
}
