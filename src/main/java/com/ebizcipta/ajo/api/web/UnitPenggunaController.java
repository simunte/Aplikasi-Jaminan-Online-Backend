package com.ebizcipta.ajo.api.web;

import com.ebizcipta.ajo.api.service.UnitPenggunaService;
import com.ebizcipta.ajo.api.service.dto.*;
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
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Api(value="Master Data Unit Pengguna")
public class UnitPenggunaController {

    private final UnitPenggunaService unitPenggunaService;
    private final AuditTrailUtil auditTrailUtil;

    public UnitPenggunaController(UnitPenggunaService unitPenggunaService, AuditTrailUtil auditTrailUtil) {
        this.unitPenggunaService = unitPenggunaService;
        this.auditTrailUtil = auditTrailUtil;
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Manual Data Transfer_READ,Master Data - Unit Pengguna_READ')")
    @GetMapping("/pengguna")
    @ApiOperation("get All Unit Pengguna")
    public ResponseEntity<List<UnitPenggunaVIewDTO>> getAllUnitPengguna(@RequestParam(value = "status", required = false) String status) {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Unit Pengguna", "getAllUnitPengguna");
        final List<UnitPenggunaVIewDTO> result = unitPenggunaService.findAllUnitPengguna(status);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Unit Pengguna", "getAllUnitPengguna");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Master Data - Unit Pengguna_CREATE')")
    @PostMapping("/pengguna")
    @ApiOperation("Add Unit Pengguna")
    public ResponseEntity<Boolean> addJenisUnitPengguna(@Valid @RequestBody UnitPenggunaDTO unitPenggunaDTO) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Unit Pengguna", "addJenisUnitPengguna");
        Boolean result = unitPenggunaService.saveUnitPengguna(unitPenggunaDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Unit Pengguna", "addJenisUnitPengguna");
        return ResponseEntity.created(new URI("/api/v1/pengguna"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Master Data - Unit Pengguna_UPDATE')")
    @PutMapping("/pengguna")
    @ApiOperation("Update Unit Pengguna")
    public ResponseEntity<Boolean> updateUnitPengguna(@Valid @RequestBody UnitPenggunaDTO unitPenggunaDTO) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Unit Pengguna", "updateUnitPengguna");
        Boolean result = unitPenggunaService.saveUnitPengguna(unitPenggunaDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Unit Pengguna", "updateUnitPengguna");
        return ResponseEntity.created(new URI("/api/v1/pengguna"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Manual Data Transfer_READ,Master Data - Unit Pengguna_READ')")
    @GetMapping("/pengguna/{id}")
    @ApiOperation("Get Detail UnitPengguna")
    public ResponseEntity<Optional<UnitPenggunaVIewDTO>> getUnitPenggunaById(@PathVariable Long id){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Unit Pengguna", "getUnitPenggunaById");
        final Optional<UnitPenggunaVIewDTO> result = unitPenggunaService.findJUnitPenggunaById(id);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Unit Pengguna", "getUnitPenggunaById");
        return ResponseEntity.ok().body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Master Data - Unit Pengguna_APPROVE')")
    @PostMapping("/pengguna/approval")
    @ApiOperation("Approval UnitPengguna")
    public ResponseEntity<Boolean> approvalUnitPengguna(@Valid @RequestBody GlobalApprovalDTO globalApprovalDTO) throws URISyntaxException{
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Unit Pengguna", "approvalUnitPengguna");
        Boolean result = unitPenggunaService.approvalUnitPengguna(globalApprovalDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Unit Pengguna", "approvalUnitPengguna");
        return ResponseEntity.created(new URI("/api/v1/pengguna"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Master Data - Unit Pengguna_DELETE')")
    @DeleteMapping("/pengguna")
    @ApiOperation("Delete UnitPengguna")
    public ResponseEntity<Boolean> deleteUnitPengguna(@Valid @RequestBody List<IdDTO> idDTO){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Unit Pengguna", "deleteUnitPengguna");
        Boolean result = unitPenggunaService.deleteUnitPengguna(idDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Unit Pengguna", "deleteUnitPengguna");
        return ResponseEntity.ok().body(result);
    }
}
