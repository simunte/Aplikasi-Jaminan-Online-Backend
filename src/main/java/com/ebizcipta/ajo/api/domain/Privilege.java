package com.ebizcipta.ajo.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "privilege")
public class Privilege extends Base {

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @NotNull
    @Column(name = "create_access", nullable = false)
    private Boolean create;

    @NotNull
    @Column(name = "read_access", nullable = false)
    private Boolean read;

    @NotNull
    @Column(name = "update_access", nullable = false)
    private Boolean update;

    @NotNull
    @Column(name = "delete_access", nullable = false)
    private Boolean delete;

    @NotNull
    @Column(name = "approval_access", nullable = false)
    private Boolean approval;
}
