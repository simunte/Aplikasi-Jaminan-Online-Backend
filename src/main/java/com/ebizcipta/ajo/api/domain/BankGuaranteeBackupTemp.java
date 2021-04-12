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
@Table(name = "bank_guarantee_backup_temp")
public class BankGuaranteeBackupTemp extends Base{
    @NotNull
    @Column(name = "nomor_jaminan")
    private String  nomorJaminan;

    @Column(name = "nomor_amendment")
    private String nomorAmentmend;

    @ManyToOne
    @JoinColumn(name = "currency")
    private Currency currency;

    @Column(name = "tanggal_berlaku")
    private Long tanggalBerlaku;

    @Column(name = "tanggal_berakhir")
    private Long tanggalBerakhir;

    @ManyToOne
    @JoinColumn(name = "jenis_jaminan")
    private JenisJaminan jenisJaminan;

    @Column(name = "nilai_jaminan")
    private BigDecimal nilaiJaminan;

    @ManyToOne
    @JoinColumn(name = "beneficiary")
    private Beneficiary beneficiary;

    private String applicant;

    @Column(name = "tanggal_terbit")
    private Long tanggalTerbit;

    @Column(name = "tanggal_batas_claim")
    private Long tanggalBatasClaim;

    @Column(name = "tenggang_waktu_claim")
    private Integer tenggangWaktuClaim;

    @Column(name = "bg_status")
    private String bgStatus;
}
