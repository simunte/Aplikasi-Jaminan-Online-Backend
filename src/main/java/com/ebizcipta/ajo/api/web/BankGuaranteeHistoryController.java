package com.ebizcipta.ajo.api.web;

import com.ebizcipta.ajo.api.service.BankGuaranteeHistoryService;
import com.ebizcipta.ajo.api.service.dto.BankGuaranteeHistoryDTO;
import com.ebizcipta.ajo.api.service.dto.BeneficiaryDTO;
import com.ebizcipta.ajo.api.service.dto.ViewBankGuaranteeHistoryDTO;
import com.ebizcipta.ajo.api.util.AuditTrailUtil;
import com.ebizcipta.ajo.api.util.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Api(value="Bank Guarantee History")
public class BankGuaranteeHistoryController {
    private final BankGuaranteeHistoryService bankGuaranteeHistoryService;
    private final AuditTrailUtil auditTrailUtil;

    public BankGuaranteeHistoryController(BankGuaranteeHistoryService bankGuaranteeHistoryService, AuditTrailUtil auditTrailUtil) {
        this.bankGuaranteeHistoryService = bankGuaranteeHistoryService;
        this.auditTrailUtil = auditTrailUtil;
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Manual Data Transfer_READ')")
    @GetMapping("/history")
    @ApiOperation("get All Bank Guarantee History")
    public ResponseEntity<List<ViewBankGuaranteeHistoryDTO>> getAllHistory() {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Bank Guarantee History", "getAllHistory");
        final List<ViewBankGuaranteeHistoryDTO> result = bankGuaranteeHistoryService.findAllHistory();
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Bank Guarantee History", "getAllHistory");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Manual Data Transfer_READ')")
    @GetMapping("/history/{idJaminan}")
    @ApiOperation("get All Bank Guarantee History By Nomor Jaminan")
    public ResponseEntity<List<ViewBankGuaranteeHistoryDTO>> getAllHistoryByNomorJaminan(@PathVariable Long idJaminan) {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Bank Guarantee History", "getAllHistoryByNomorJaminan");
        final List<ViewBankGuaranteeHistoryDTO> result = bankGuaranteeHistoryService.findByNomorJaminan(idJaminan);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Bank Guarantee History", "getAllHistoryByNomorJaminan");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_CREATE,Bank Guarantee_UPDATE')")
    @PostMapping("/history")
    @ApiOperation("Add Bank Guarantee History")
    public ResponseEntity<Boolean> addHistory(@Valid @RequestBody BankGuaranteeHistoryDTO bankGuaranteeHistoryDTO) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Bank Guarantee History", "addHistory");
        Boolean result = bankGuaranteeHistoryService.saveHistory(bankGuaranteeHistoryDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Bank Guarantee History", "addHistory");
        return ResponseEntity.created(new URI("/api/v1/history"))
                .body(result);
    }
}
