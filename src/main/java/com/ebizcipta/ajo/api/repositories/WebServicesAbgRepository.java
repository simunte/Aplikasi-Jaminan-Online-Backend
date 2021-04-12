package com.ebizcipta.ajo.api.repositories;

import com.ebizcipta.ajo.api.domain.Beneficiary;
import com.ebizcipta.ajo.api.domain.WebServicesAbg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WebServicesAbgRepository extends JpaRepository<WebServicesAbg, Long>{
    Optional<WebServicesAbg> findByBeneficiary(Beneficiary beneficiary);
    List<WebServicesAbg> findByStatusAndModifiedByNot(String status, String userLogin);
    List<WebServicesAbg> findByStatusNot(String status);
}
