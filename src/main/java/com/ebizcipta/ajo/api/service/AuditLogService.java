package com.ebizcipta.ajo.api.service;

import com.ebizcipta.ajo.api.domain.AuditLog;
import com.ebizcipta.ajo.api.service.dto.AuditLogDTO;

import java.time.Instant;
import java.util.List;

public interface AuditLogService {
    void saveAuditLog(AuditLog auditLog);
    List<AuditLogDTO> getListAuditLog(Long startDate, Long endDate, String module);
}
