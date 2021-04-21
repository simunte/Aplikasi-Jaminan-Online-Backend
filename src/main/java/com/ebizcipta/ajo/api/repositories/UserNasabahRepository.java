package com.ebizcipta.ajo.api.repositories;

import com.ebizcipta.ajo.api.domain.UserNasabah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserNasabahRepository extends JpaRepository<UserNasabah, Long> {
    Optional<UserNasabah> findDistinctTopByUsername(String username);
}
