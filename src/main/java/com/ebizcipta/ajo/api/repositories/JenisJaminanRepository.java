package com.ebizcipta.ajo.api.repositories;

import com.ebizcipta.ajo.api.domain.JenisJaminan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JenisJaminanRepository extends JpaRepository<JenisJaminan, Long>{
    Optional<JenisJaminan> findByJenisJaminan(String jenisJaminan);
    Optional<JenisJaminan> findByCodeJaminanStaging(String kodeJaminan);
    List<JenisJaminan> findByStatusAndModifiedByNot(String status, String userLogin);
    List<JenisJaminan> findByStatusNot(String status);
    List<JenisJaminan> findByCodeJaminanOrJenisJaminanEqualsIgnoreCase(String codeJaminan , String jenisJaminan);
    Optional<JenisJaminan> findByCodeJaminanEqualsIgnoreCase(String code);
}


