package com.ebizcipta.ajo.api.repositories;

import com.ebizcipta.ajo.api.domain.BankGuaranteeHistory;
import com.ebizcipta.ajo.api.domain.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankGuaranteeHistoryRepository extends JpaRepository<BankGuaranteeHistory, Long>{
    List<BankGuaranteeHistory> findByRegistrationOrderByCreationDateAsc(Registration registration);

    Optional<BankGuaranteeHistory> findByRegistrationAndBgStatus(Registration registration, String bgStatus);
}
