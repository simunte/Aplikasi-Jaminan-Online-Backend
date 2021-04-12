package com.ebizcipta.ajo.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "password_history")
public class PasswordHistory extends Base {

    @ManyToOne
    @JoinColumn(name = "users")
    private User user;

    @Column(name = "user_password")
    private String password;

}
