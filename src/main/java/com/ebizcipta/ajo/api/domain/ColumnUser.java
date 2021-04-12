package com.ebizcipta.ajo.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "column_user")
public class ColumnUser extends Base{

    @ManyToOne
    @JoinColumn(name = "users")
    private User user;

    @Lob
    @Column(name = "list_of_column", length = 512)
    private String listColumn;
}
