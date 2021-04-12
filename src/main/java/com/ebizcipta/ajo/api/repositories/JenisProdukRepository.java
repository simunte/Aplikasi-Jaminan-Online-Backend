package com.ebizcipta.ajo.api.repositories;

import com.ebizcipta.ajo.api.domain.JenisProduk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JenisProdukRepository extends JpaRepository<JenisProduk, Long> {
    List<JenisProduk> findByStatusAndModifiedByNot(String status, String userLogin);
    List<JenisProduk> findByStatusNot(String status);
    List<JenisProduk> findByCodeProdukOrJenisProdukEqualsIgnoreCase(String codeProduk ,String jenisProduk);
    Optional<JenisProduk> findByCodeProdukEqualsIgnoreCase(String code);
}
