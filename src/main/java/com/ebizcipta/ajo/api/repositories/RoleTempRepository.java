package com.ebizcipta.ajo.api.repositories;

import com.ebizcipta.ajo.api.domain.Role;
import com.ebizcipta.ajo.api.domain.RoleTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleTempRepository extends JpaRepository<RoleTemp, Long> {
    RoleTemp findByRoleId(Integer id);
}
