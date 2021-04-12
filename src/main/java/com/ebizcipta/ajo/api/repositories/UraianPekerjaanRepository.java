package com.ebizcipta.ajo.api.repositories;

import com.ebizcipta.ajo.api.domain.UraianPekerjaan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UraianPekerjaanRepository extends JpaRepository<UraianPekerjaan, Long> {
}
