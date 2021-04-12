package com.ebizcipta.ajo.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "jenis_produk")
public class JenisProduk extends Base{


    @Column(name = "jenis_produk")
    private String jenisProduk;

    @Column(name = "code_produk")
    private String codeProduk;

    private String status;

    private String description;

    private Boolean activated;
    
    private String notes;
}
