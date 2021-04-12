package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JenisProdukViewDTO extends BaseDTO{
    private Long id;
    @JsonProperty(value = "jenis_produk")
    private String jenisProduk;

    @JsonProperty(value = "code_produk")
    private String codeProduk;

    private String status;

    private String description;

    private Boolean activated;
    private String notes;
}
