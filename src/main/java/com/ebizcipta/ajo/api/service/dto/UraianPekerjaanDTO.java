package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UraianPekerjaanDTO {
    private Long id;
    @JsonProperty(value = "uraian_pekerjaan")
    private String uraianPekerjaan;
    private String description;
}
