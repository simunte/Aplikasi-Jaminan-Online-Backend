package com.ebizcipta.ajo.api.service.dto;

import com.ebizcipta.ajo.api.domain.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Lob;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class RegistrationDTO {

    private Long id;

    @JsonProperty(value = "nomor_jaminan")
    private String  nomorJaminan;

    @JsonProperty(value = "nomor_amendment")
    private String nomorAmentmend;

    @JsonProperty(value = "jenis_produk_id")
    private Long jenisProdukId;

    @JsonProperty(value = "jenis_jaminan_id")
    private Long jenisJaminanId;

    @JsonProperty(value = "beneficiary_id")
    private Long beneficiaryId;

    @JsonProperty(value = "unit_pengguna_id")
    private Long unitPenggunaId;

    @JsonProperty(value = "nama_bank_penerbit")
    private String namaBankPenerbit;

    private String applicant;

    @JsonProperty(value = "alamat_bank_penerbit_id")
    private Long alamatBankPenerbitId;

    @Size(max = 50)
    @JsonProperty(value = "nomor_kontrak")
    private String nomorKontrak;

    @Lob
    @JsonProperty(value = "uraian_pekerjaan")
    private String uraianPekerjaan;

    @JsonProperty(value = "currency_id")
    private Long currencyId;

    @JsonProperty(value = "nilai_jaminan")
    private BigDecimal nilaiJaminan;

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

    @Lob
    private String notes;

}
