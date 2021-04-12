package com.ebizcipta.ajo.api.web;

import com.ebizcipta.ajo.api.service.DashboardService;
import com.ebizcipta.ajo.api.service.dto.DashboardDTO;
import com.ebizcipta.ajo.api.service.dto.DashboardITDTO;
import com.ebizcipta.ajo.api.util.AuditTrailUtil;
import com.ebizcipta.ajo.api.util.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Api(value="Bank Guarantee Registration")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private AuditTrailUtil auditTrailUtil;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/dashboard")
    @ApiOperation("Dashboard")
    public ResponseEntity<Optional<DashboardITDTO>> dashboard(){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Bank Guarantee Registration", "dashboard");
        final Optional<DashboardITDTO> result = dashboardService.getCountByStatus();
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Bank Guarantee Registration", "dashboard");
        return ResponseEntity.ok()
                .body(result);
    }
}
