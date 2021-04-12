package com.ebizcipta.ajo.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role_temp")
public class RoleTemp extends Base {

    private Integer roleId;

    @Lob
    @Column(name = "data", length = 5000)
    private String data;

    @Lob
    private String note;

    private String name;
}
