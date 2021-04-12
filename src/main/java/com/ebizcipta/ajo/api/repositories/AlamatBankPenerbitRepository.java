package com.ebizcipta.ajo.api.repositories;

import com.ebizcipta.ajo.api.domain.AlamatBankPenerbit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlamatBankPenerbitRepository extends JpaRepository<AlamatBankPenerbit, Long> {
    List<AlamatBankPenerbit> findByStatusAndModifiedByNot(String status, String userLogin);
    List<AlamatBankPenerbit> findByStatusNot(String status);
    List<AlamatBankPenerbit> findByCodeOrCabangEqualsIgnoreCaseAndStatusNotIn(String code ,  String alamatBankPenerbit, List<String> status);
    Optional<AlamatBankPenerbit> findByCodeEqualsIgnoreCase(String code);
}
