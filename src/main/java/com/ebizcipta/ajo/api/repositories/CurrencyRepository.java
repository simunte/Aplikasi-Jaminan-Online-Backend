package com.ebizcipta.ajo.api.repositories;

import com.ebizcipta.ajo.api.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Optional<Currency> findByCurrency(String currency);
    List<Currency> findByStatusAndModifiedByNot(String status, String userLogin);
    List<Currency> findByStatusNot(String status);
    Currency findByCurrencyEqualsIgnoreCaseAndStatusNotIn(String currency, List<String> status);
    Optional<Currency> findByCurrencyEqualsIgnoreCase(String code);
}
