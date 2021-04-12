package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DashboardDTO {

    @JsonProperty(value = "status")
    private String status;

    @JsonProperty(value = "jumlah_task")
    private String jumlah;
}
