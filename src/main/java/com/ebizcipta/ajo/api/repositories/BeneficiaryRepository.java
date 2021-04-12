package com.ebizcipta.ajo.api.repositories;

import com.ebizcipta.ajo.api.domain.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long>{
    Optional<Beneficiary> findByCodeBeneficiary(String namaBeneficiary);
    List<Beneficiary> findByStatusAndModifiedByNot(String status, String userLogin);
    List<Beneficiary> findByStatusNot(String Status);
    List<Beneficiary> findByStatusIn(List<String> status);
    List<Beneficiary> findByCodeBeneficiaryOrNamaBeneficiaryEqualsIgnoreCase(String codeBeneficiary , String namaBeneficiary);
    Optional<Beneficiary> findByCodeBeneficiaryEqualsIgnoreCase(String code);


}
