package com.ebizcipta.ajo.api.domain;

import com.ebizcipta.ajo.api.service.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "currency")
public class Currency extends Base{
    @Column(name = "currency")
    private String currency;
    private String status;
    private Boolean activated;
    private String description;
    private String notes;
}
