package com.ebizcipta.ajo.api.web;

import com.ebizcipta.ajo.api.service.ManualDataTransferService;
import com.ebizcipta.ajo.api.service.dto.IdDTO;
import com.ebizcipta.ajo.api.service.dto.ViewRegistrationDTO;
import com.ebizcipta.ajo.api.util.AuditTrailUtil;
import com.ebizcipta.ajo.api.util.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Api(value="Manual Data Transfer")
public class ManualDataTransferController {
    private final ManualDataTransferService manualDataTransferService;
    private final AuditTrailUtil auditTrailUtil;

    public ManualDataTransferController(ManualDataTransferService manualDataTransferService, AuditTrailUtil auditTrailUtil) {
        this.manualDataTransferService = manualDataTransferService;
        this.auditTrailUtil = auditTrailUtil;
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Manual Data Transfer_READ')")
    @GetMapping("/manual/transfer")
    @ApiOperation("get All Manual Data Transfer")
    public ResponseEntity<List<ViewRegistrationDTO>> getListManualDataTransfer() {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Manual Data Transfer", "getListManualDataTransfer");
        final List<ViewRegistrationDTO> result = manualDataTransferService.findAllBgTransfer();
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Manual Data Transfer", "getListManualDataTransfer");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Manual Data Transfer_CREATE,Manual Data Transfer_UPDATE,Manual Data Transfer_READ')")
    @PostMapping("/manual/transfer")
    @ApiOperation("Send Manual Data Transfer")
    public ResponseEntity<Boolean> sendManualDataTransfer(@Valid @RequestBody List<IdDTO> idDTO){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Manual Data Transfer", "sendManualDataTransfer");
        Boolean result = manualDataTransferService.sendManualBg(idDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Manual Data Transfer", "sendManualDataTransfer");
        return ResponseEntity.ok()
                .body(result);
    }
}
