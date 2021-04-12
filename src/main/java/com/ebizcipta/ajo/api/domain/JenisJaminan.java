package com.ebizcipta.ajo.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "jenis_jaminan")
public class JenisJaminan extends Base{


    @Column(name = "jenis_jaminan")
    private String jenisJaminan;

    @Column(name = "code_jaminan")
    private String codeJaminan;

    @Column(name = "code_jaminan_staging")
    private String codeJaminanStaging;

    private String status;

    private String description;

    private Boolean activated;

    private String notes;
}
