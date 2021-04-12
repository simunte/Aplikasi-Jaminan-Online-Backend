package com.ebizcipta.ajo.api.repositories;

import com.ebizcipta.ajo.api.domain.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByDateTimeBetweenAndModuleAndUsernameNotOrderByDateTime(Instant startDate, Instant endDate, String module, String username);
    List<AuditLog> findByDateTimeBetweenAndUsernameNotOrderByDateTime(Instant startDate, Instant endDate, String username);
}
