package com.ebizcipta.ajo.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "registration")
public class Registration extends Base{

    @Column(name = "tanggal_kirim")
    private Instant tglKirimAbg;

    @NotNull
    @Column(name = "nomor_jaminan")
    private String  nomorJaminan;

    @Column(name = "nomor_amendment")
    private String nomorAmentmend;

    @ManyToOne
    @JoinColumn(name = "jenis_produk")
    private JenisProduk jenisProduk;

    @ManyToOne
    @JoinColumn(name = "jenis_jaminan")
    private JenisJaminan jenisJaminan;

    @ManyToOne
    @JoinColumn(name = "beneficiary")
    private Beneficiary beneficiary;

    @ManyToOne
    @JoinColumn(name = "unit_pengguna")
    private UnitPengguna unitPengguna;

    @Column(name = "nama_bank_penerbit")
    private String namaBankPenerbit;

    @ManyToOne
    @JoinColumn(name = "alamat_bank_penerbit")
    private AlamatBankPenerbit alamatBankPenerbit;

    @Column(name = "nomor_kontrak")
    private String nomorKontrak;

    private String applicant;

    @Lob
    @Column(name = "uraian_pekerjaan")
    private String uraianPekerjaan;

    @ManyToOne
    @JoinColumn(name = "currency")
    private Currency currency;

    @Column(name = "nilai_jaminan")
    private BigDecimal nilaiJaminan;

    @Column(name = "tanggal_terbit")
    private Long tanggalTerbit;

    @Column(name = "tanggal_berlaku")
    private Long tanggalBerlaku;

    @Column(name = "tanggal_berakhir")
    private Long tanggalBerakhir;

    @Column(name = "tenggang_waktu_claim")
    private Integer tenggangWaktuClaim;

    @Column(name = "tanggal_batas_claim")
    private Long tanggalBatasClaim;

    @Column(name = "softcopy_jaminan_name")
    private String softCopyJaminanName;

    @Column(name = "softcopy_jaminan_url")
    private String softCopyJaminanUrl;

    @Column(name = "bg_status")
    private String bgStatus;

    @Lob
    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_approve")
    private User userApprove;

    @Column(name = "manual_bg")
    private Boolean manualBg;
}
