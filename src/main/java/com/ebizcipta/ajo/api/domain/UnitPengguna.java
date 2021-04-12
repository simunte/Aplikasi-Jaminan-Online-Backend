package com.ebizcipta.ajo.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "unit_pengguna")
public class UnitPengguna extends Base{

    @Column(name = "unit_pengguna")
    private String unitPengguna;

    @Column(name = "code_unit_pengguna")
    private String codeUnitPengguna;

    private String status;

    private String description;

    private Boolean activated;
    private String notes;

}
