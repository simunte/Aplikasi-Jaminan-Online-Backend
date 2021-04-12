package com.ebizcipta.ajo.api.repositories;

import com.ebizcipta.ajo.api.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNameLikeIgnoreCase(String name);
    List<Role> findByStatusIn(String[] status);
    List<Role> findByRoleCreateLikeIgnoreCase(String roleCreates);
    Role  findByRoleCreate(String roleCreate);

    List<Role> findByStatusAndModifiedByNot(String status, String userLogin);
}
