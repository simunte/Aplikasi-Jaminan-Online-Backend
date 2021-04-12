package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UnitPenggunaDTO {
    private Long id;
    @JsonProperty(value = "unit_pengguna")
    private String unitPengguna;
    @JsonProperty(value = "code_unit_pengguna")
    private String codeUnitPengguna;

    private String status;

    private String description;

    private Boolean activated;
}
