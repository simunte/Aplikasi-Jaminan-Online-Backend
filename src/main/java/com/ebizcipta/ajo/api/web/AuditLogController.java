package com.ebizcipta.ajo.api.web;

import com.ebizcipta.ajo.api.service.AuditLogService;
import com.ebizcipta.ajo.api.service.dto.AuditLogDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Api(value="Audit Log")
public class AuditLogController {
    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Audit Trail_READ')")
    @GetMapping("/audit/trail")
    @ApiOperation("get All Audit Log")
    public ResponseEntity<List<AuditLogDTO>> getAllAuditLog(
            @RequestParam(value = "startDate", required = true) Long startDate,
            @RequestParam(value = "endDate", required = true) Long endDate,
            @RequestParam(value = "module", required = true) String module
    ) {
        final List<AuditLogDTO> result = auditLogService.getListAuditLog(startDate, endDate, module);
        return ResponseEntity.ok()
                .body(result);
    }
}
