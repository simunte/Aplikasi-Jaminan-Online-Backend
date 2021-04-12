package com.ebizcipta.ajo.api.repositories;

import com.ebizcipta.ajo.api.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByNameLikeIgnoreCase(String name);
}
