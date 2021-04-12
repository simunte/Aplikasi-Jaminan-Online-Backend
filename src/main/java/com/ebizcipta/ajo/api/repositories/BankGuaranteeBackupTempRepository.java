package com.ebizcipta.ajo.api.repositories;

import com.ebizcipta.ajo.api.domain.BankGuaranteeBackupTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankGuaranteeBackupTempRepository extends JpaRepository<BankGuaranteeBackupTemp, Long> {
    Optional<BankGuaranteeBackupTemp> findTop1ByNomorJaminanOrderByNomorAmentmendDesc(String nomorJaminan);
}
