package com.ebizcipta.ajo.api.web;


import com.ebizcipta.ajo.api.service.BeneficiaryService;
import com.ebizcipta.ajo.api.service.dto.*;
import com.ebizcipta.ajo.api.util.AuditTrailUtil;
import com.ebizcipta.ajo.api.util.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Api(value="Master Data Beneficiary")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;
    private final AuditTrailUtil auditTrailUtil;

    public BeneficiaryController(BeneficiaryService beneficiaryService, AuditTrailUtil auditTrailUtil) {
        this.beneficiaryService = beneficiaryService;
        this.auditTrailUtil = auditTrailUtil;
    }
    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Manual Data Transfer_READ,Configuration - Beneficiary_READ,User Access Management - User Access_READ')")
    @GetMapping("/beneficiary")
    @ApiOperation("get All Beneficiary")
    public ResponseEntity<List<BeneficiaryViewDTO>> getAllBeneficiary(@RequestParam(value = "status", required = false) String status) {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Beneficiary", "getAllBeneficiary");
        final List<BeneficiaryViewDTO> result = beneficiaryService.findAllBeneficiary(status);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Beneficiary", "getAllBeneficiary");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Configuration - Beneficiary_CREATE')")
    @PostMapping("/beneficiary")
    @ApiOperation("Add Beneficiary")
    public ResponseEntity<Boolean> addBeneficiary(@Valid @RequestBody BeneficiaryDTO beneficiaryDTO) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Beneficiary", "addBeneficiary");
        Boolean result = beneficiaryService.saveBeneficiary(beneficiaryDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Beneficiary", "addBeneficiary");
        return ResponseEntity.created(new URI("/api/v1/beneficiary"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Configuration - Beneficiary_UPDATE')")
    @PutMapping("/beneficiary")
    @ApiOperation("Update Beneficiary")
    public ResponseEntity<Boolean> updateBeneficiary(@Valid @RequestBody BeneficiaryDTO beneficiaryDTO) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Beneficiary", "updateBeneficiary");
        Boolean result = beneficiaryService.saveBeneficiary(beneficiaryDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Beneficiary", "updateBeneficiary");
        return ResponseEntity.created(new URI("/api/v1/beneficiary"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Manual Data Transfer_READ,Configuration - Beneficiary_READ,User Access Management - User Access_READ')")
    @GetMapping("/beneficiary/{id}")
    @ApiOperation("Get Detail Beneficiary")
    public ResponseEntity<Optional<BeneficiaryViewDTO>> getBeneficiaryById(@PathVariable Long id){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Beneficiary", "getBeneficiaryById");
        final Optional<BeneficiaryViewDTO> result = beneficiaryService.findBeneficiaryById(id);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Beneficiary", "getBeneficiaryById");
        return ResponseEntity.ok().body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Configuration - Beneficiary_APPROVE')")
    @PostMapping("/beneficiary/approval")
    @ApiOperation("Approval Beneficiary")
    public ResponseEntity<Boolean> approvalBeneficiary(@Valid @RequestBody GlobalApprovalDTO globalApprovalDTO) throws URISyntaxException{
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Beneficiary", "approvalBeneficiary");
        Boolean result = beneficiaryService.approvalBeneficiary(globalApprovalDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Beneficiary", "approvalBeneficiary");
        return ResponseEntity.created(new URI("/api/v1/beneficiary/approval"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Configuration - Beneficiary_DELETE')")
    @DeleteMapping("/beneficiary")
    @ApiOperation("Delete Beneficiary")
    public ResponseEntity<Boolean> deleteBeneficiary(@Valid @RequestBody List<IdDTO> idDTO){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Beneficiary", "deleteBeneficiary");
        Boolean result = beneficiaryService.deleteBeneficiary(idDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Beneficiary", "deleteBeneficiary");
        return ResponseEntity.ok().body(result);
    }
}
