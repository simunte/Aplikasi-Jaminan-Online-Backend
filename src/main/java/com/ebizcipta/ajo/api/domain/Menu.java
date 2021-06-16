package com.ebizcipta.ajo.api.domain;

/**
 * A menu model.
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "menu")
public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(length = 50, nullable = false)
    private String name;

    @Size(min = 3, max = 50)
    @Column(name = "head_menu", length = 50)
    private String headMenu;

    @Size(max = 100)
    @Column(length = 100)
    private String description;

    @Size(min = 3, max = 50)
    @Column(name = "alias_menu",length = 50)
    private String aliasMenu;
}
