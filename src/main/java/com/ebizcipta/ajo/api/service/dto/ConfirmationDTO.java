package com.ebizcipta.ajo.api.service.dto;

import com.ebizcipta.ajo.api.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

@Data
public class ConfirmationDTO {

    @JsonProperty("id_jaminan")
    private Long idJaminan;

    @JsonProperty(value = "nomor_konfirmasi_bank")
    private String nomorKonfirmasiBank;

    @JsonProperty(value = "username_penanda_tangan")
    private String username;

    @JsonProperty(value = "tanggal_surat_konfirmasi")
    private Long tanggalSuratKonfirmasi;
}
