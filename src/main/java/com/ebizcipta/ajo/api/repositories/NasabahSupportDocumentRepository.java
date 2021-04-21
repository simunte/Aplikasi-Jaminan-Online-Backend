package com.ebizcipta.ajo.api.repositories;

import com.ebizcipta.ajo.api.domain.NasabahSupportDocument;
import com.ebizcipta.ajo.api.domain.UserNasabah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NasabahSupportDocumentRepository extends JpaRepository<NasabahSupportDocument, Long> {
    List<NasabahSupportDocument> findByUserNasabahAndIsDeleted(UserNasabah userNasabah, Boolean delete);
}
