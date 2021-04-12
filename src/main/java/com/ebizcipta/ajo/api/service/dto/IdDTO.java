package com.ebizcipta.ajo.api.service.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class IdDTO{
    @NotBlank
    private Long id;
}
