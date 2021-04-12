package com.ebizcipta.ajo.api.repositories;

import com.ebizcipta.ajo.api.domain.UnitPengguna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnitPenggunaRepository extends JpaRepository<UnitPengguna, Long> {
    Optional<UnitPengguna> findByUnitPengguna(String unitPengguna);
    List<UnitPengguna> findByStatusAndModifiedByNot(String status, String userLogin);
    List<UnitPengguna> findByStatusNot(String status);
    List<UnitPengguna> findByCodeUnitPenggunaOrUnitPenggunaEqualsIgnoreCase(String codeUnitPengguna, String unitPengguna);
    Optional<UnitPengguna> findByCodeUnitPenggunaEqualsIgnoreCase(String code);
}
