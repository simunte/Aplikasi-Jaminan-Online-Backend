package com.ebizcipta.ajo.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_nasabah")
public class UserNasabah extends Base{
    private String username;
    @Column(name = "complete_name")
    private String completeName;
    @Column(name = "company_name")
    private String companyName;
    private String position;
    @Column(name = "npwp_number")
    private String npwpNumber;
    @Column(name = "id_number")
    private String idNumber;
    @Column(name = "email")
    private String employeeEmail;
}
