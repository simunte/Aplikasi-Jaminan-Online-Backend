package com.ebizcipta.ajo.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(name = "idx_role_name", columnList = "name")
})
public class Role extends Base {

    private String name;
    private String description;

    @NotNull
    @Column(nullable = false)
    private Boolean activated;

    @ManyToOne
    @JoinColumn(name = "beneficiary")
    private Beneficiary beneficiary;

    @Column(name="Status")
    private String status;

    @Column(name = "role_create")
    private String roleCreate;

    @Column(name = "code")
    private String  code;

    @Lob
    private String note;

    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "privilege_role", joinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id") }, inverseJoinColumns = {
            @JoinColumn(name = "privilege_id", referencedColumnName = "id") })
    private List<Privilege> privileges;
}
