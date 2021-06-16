package com.ebizcipta.ajo.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A user model.
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends Base implements Serializable, UserDetails {

    private String username;

    private String email;

    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String position;

    private String status;

    private String signature;

    private Boolean isFirstLogin;

    private Boolean isExternal;

    private boolean isAccountNonExpired;

    private boolean isAccountNonLocked;

    private boolean isCredentialsNonExpired;

    private boolean isEnabled;

    private Integer countDisabled;

    private Instant lastLoggedIn;

    private Instant lastChangePassword;

    private Boolean isForceChangePassword;

    private String updateManualBy;

    @Lob
    private String note;

    @Column(name = "need_approval_or_reject")
    private String needApprovalOrReject;

    @ManyToOne
    @JoinColumn(name = "beneficiary")
    private Beneficiary beneficiary;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns =
            @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @PrePersist
    public void prePersist(){
        this.isAccountNonExpired = false;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = false;
        this.isFirstLogin=true;
        this.countDisabled=0;
        this.isForceChangePassword=false;
    }
}
