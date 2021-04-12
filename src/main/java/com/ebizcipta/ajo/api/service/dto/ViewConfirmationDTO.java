package com.ebizcipta.ajo.api.service.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ViewConfirmationDTO {

    @JsonProperty(value = "nomor_jaminan")
    private String nomorJaminan;

    @JsonProperty(value = "nomor_konfirmasi_bank")
    private String nomorKonfirmasiBank;

    @JsonProperty(value = "penanda_tangan_konfirmasi")
    private String penandaTangan;

    @JsonProperty(value = "jabatan_penanda_tangan")
    private String jabatanPenandaTangan;

    @JsonProperty(value = "tanggal_surat_konfirmasi")
    private Long tanggalSuratKonfirmasi;

}
