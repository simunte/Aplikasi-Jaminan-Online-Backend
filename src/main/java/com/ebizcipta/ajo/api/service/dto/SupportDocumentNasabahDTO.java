package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SupportDocumentNasabahDTO {
    @JsonProperty(value = "jenis_file")
    private String jenisFile;

    @JsonProperty(value = "nama_file")
    private String namaFile;

    @JsonProperty(value = "nama_file_encrypt")
    private String namaFileEncrypt;

    @JsonProperty(value = "url_file")
    private String urlFile;
}
