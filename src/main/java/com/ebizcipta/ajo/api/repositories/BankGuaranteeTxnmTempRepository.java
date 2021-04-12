package com.ebizcipta.ajo.api.repositories;

import com.ebizcipta.ajo.api.domain.BankGuaranteeTxnmTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BankGuaranteeTxnmTempRepository extends JpaRepository<BankGuaranteeTxnmTemp, Long> {
    List<BankGuaranteeTxnmTemp> findByTransactionDateBetween(Date yesterday, Date toDay);

    @Query(value = "SELECT * FROM bank_guarantee_txnm_temp \n" +
            "WHERE (to_date(TRIM(c124_txnm_entry_dt), 'YYYYMMDD') BETWEEN TO_DATE(:beforeDate, 'YYYYMMDD') AND TO_DATE(:newDate, 'YYYYMMDD')) \n" +
            "AND TRIM(C002_TXNM_REF_NBR) LIKE %:bankGuaranti% AND TRIM(c124_txnm_entry_dt) != '00000000'\n",
            nativeQuery = true)
    List<BankGuaranteeTxnmTemp> getListByTransactionDate(@Param("beforeDate") String beforeDate, @Param("newDate") String newDate, @Param("bankGuaranti") String bankGuaranti);

    @Query(value = "SELECT * FROM bank_guarantee_txnm_temp\n" +
            "WHERE (transaction_date BETWEEN :beforeDate AND :newDate)\n",
            nativeQuery = true)
    List<BankGuaranteeTxnmTemp> getListByTransactionDateDummy(@Param("beforeDate") Long beforeDate, @Param("newDate") Long newDate);
}
