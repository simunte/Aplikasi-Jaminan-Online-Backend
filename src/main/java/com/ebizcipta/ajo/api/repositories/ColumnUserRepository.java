package com.ebizcipta.ajo.api.repositories;

import com.ebizcipta.ajo.api.domain.ColumnUser;
import com.ebizcipta.ajo.api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColumnUserRepository extends JpaRepository<ColumnUser, Long> {
    Optional<ColumnUser> findByUserUsername(String username);
    Optional<ColumnUser> findByUser(User user);
}
