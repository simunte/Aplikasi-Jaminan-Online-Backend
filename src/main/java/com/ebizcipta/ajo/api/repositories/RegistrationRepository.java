package com.ebizcipta.ajo.api.repositories;

import com.ebizcipta.ajo.api.domain.Registration;
import com.ebizcipta.ajo.api.util.Constants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    Optional<Registration> findByNomorJaminan(String nomorJaminan);
    List<Registration> findByBgStatusIn(List<String> status);
    List<Registration> findByBgStatus(String status);
    List<Registration> findByTglKirimAbgLessThanAndBgStatus(Instant date, String status);
    List<Registration> findByTanggalBatasClaimLessThanAndBgStatus(Long date, String status);
    Optional<Registration> findTop1ByNomorJaminanAndBgStatusInOrderByNomorAmentmendDesc(String nomorJaminan, List<String> bgStatus);
    Optional<Registration> findTop1ByNomorJaminanAndBgStatusNotOrderByNomorAmentmendDesc(String nomorJaminan, String bgStatus);
    Optional<Registration> findTop1ByOrderByIdDesc();
    Optional<Registration> findTop1ByNomorJaminanAndNomorAmentmendAndBgStatusNotOrderByNomorAmentmendDesc(String nomorJaminan, String nomorAmendment, String bgStatus);
    List<Registration> findByNomorJaminanAndBgStatusNot(String nomorJaminan, String bgStatus);
}
