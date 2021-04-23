package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Lob;
import java.math.BigDecimal;

@Data
public class ViewRegistrationDTO {
    private Long id;

    @JsonProperty(value = "nomor_jaminan")
    private String  nomorJaminan;

    @JsonProperty(value = "nomor_amendment")
    private String nomorAmentmend;

    @JsonProperty(value = "jenis_produk")
    private String jenisProduk;

    @JsonProperty(value = "jenis_jaminan")
    private String jenisJaminan;
	
	@JsonProperty(value = "doc_jenis_jaminan")
    private String docJenisJaminan;

    @JsonProperty(value = "nama_beneficiary")
    private String beneficiary;

    @JsonProperty(value = "unit_pengguna")
    private String unitPengguna;

    @JsonProperty(value = "nama_bank_penerbit")
    private String namaBankPenerbit;

    @JsonProperty(value = "alamat_bank_penerbit")
    private String alamatBankPenerbit;

    private String applicant;

    @JsonProperty(value = "nomor_kontrak")
    private String nomorKontrak;

    @JsonProperty(value = "uraian_pekerjaan")
    private String uraianPekerjaan;

    @JsonProperty(value = "currency")
    private String currency;

    @JsonProperty(value = "nilai_jaminan")
    private BigDecimal nilaiJaminan;
	
	@JsonProperty(value = "nilai_kontrak")
    private BigDecimal nilaiKontrak;

    @JsonProperty(value = "tanggal_terbit")
    private Long tanggalTerbit;

    @JsonProperty(value = "tanggal_berlaku")
    private Long tanggalBerlaku;

    @JsonProperty(value = "tanggal_berakhir")
    private Long tanggalBerakhir;

    @JsonProperty(value = "tenggang_waktu_claim")
    private Integer tenggangWaktuClaim;

    @JsonProperty(value  = "tanggal_batas_claim")
    private Long tanggalBatasClaim;

    @JsonProperty(value = "softcopy_jaminan_name")
    private String softCopyJaminanName;

    @JsonProperty(value = "softcopy_jaminan_url")
    private String softCopyJaminanUrl;

    @JsonProperty(value = "bank_guarantee_status")
    private String bgStatus;

    @Lob
    private String notes;

    @JsonProperty(value = "row_color")
    private String rowColor;

    @JsonProperty(value = "manual_bg")
    private Boolean manualBg;
}
