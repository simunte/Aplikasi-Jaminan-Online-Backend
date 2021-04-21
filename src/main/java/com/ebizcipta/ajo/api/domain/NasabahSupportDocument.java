package com.ebizcipta.ajo.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "support_document")
public class NasabahSupportDocument extends Base{

    @ManyToOne
    @JoinColumn(name = "nasabah", nullable = false)
    private UserNasabah userNasabah;

    @Column(name = "jenis_file")
    private String jenisFile;

    @Column(name = "nama_file")
    private String namaFile;

    @Column(name = "nama_file_encrypt")
    private String namaFileEncrypt;

    @Column(name = "url_file")
    private String urlFile;

    private Boolean isDeleted;
}
