package com.ebizcipta.ajo.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "alamat_bank_penerbit")
public class AlamatBankPenerbit extends Base {

    @Lob
    @Column(name = "alamat_bank_Penerbit")
    private String alamatBankPenerbit;
    private String code;
    private String cabang;
    private String status;

    private String description;

    private Boolean activated;
}
