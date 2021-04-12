package com.ebizcipta.ajo.api.repositories;

import com.ebizcipta.ajo.api.domain.BankGuaranteeTxnhTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface BankGuaranteeTxnhTempRepository extends JpaRepository<BankGuaranteeTxnhTemp, Long> {
    @Query(value = "SELECT DISTINCT(C003_TXNH_REF_NBR) FROM bank_guarantee_txnh_temp\n" +
            "WHERE (transaction_date BETWEEN :beforeDate AND :newDate)\n" +
            "AND C039_USR_DEF_5 LIKE %:typeTxnh%\n",
            nativeQuery = true)
    List<BankGuaranteeTxnhTemp> getListTxnh(@Param("beforeDate") Long beforeDate, @Param("newDate") Long newDate, @Param("typeTxnh") String typeTxnh);

    @Query(value = "SELECT DISTINCT(C003_TXNH_REF_NBR) FROM bank_guarantee_txnh_temp\n" +
            "WHERE (to_date(TRIM(C027_TXNH_TXN_DT), 'YYYYMMDD') BETWEEN TO_DATE(:beforeDate, 'YYYYMMDD') AND TO_DATE(:newDate, 'YYYYMMDD'))\n" +
            "AND TRIM(C039_USR_DEF_5) LIKE %:typeTxnh% AND TRIM(C027_TXNH_TXN_DT) != '00000000'\n",
            nativeQuery = true)
    List<String> getListTxnhString(@Param("beforeDate") String beforeDate, @Param("newDate") String newDate, @Param("typeTxnh") String typeTxnh);

}
