package com.ebizcipta.ajo.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "confirmation")
public class Confirmation extends Base{

    @ManyToOne
    @JoinColumn(name = "bg_registration")
    private Registration registration;

    @Column(name = "nomor_konfirmasi_bank")
    private String nomorKonfirmasiBank;

    @ManyToOne
    @JoinColumn(name = "penanda_tangan")
    private User user;

    @Column(name = "tanggal_surat_konfirmasi")
    private Instant tanggalSuratKonfirmasi;

    @Column(name = "soft_copy_surat_konfirmasi")
    private String softCopySuratKonfirmasi;

    @Column(name = "soft_copy_surat_konfirmasi_with_ttd")
    private String softCopySuratKonfirmasiWithTtd;
}
