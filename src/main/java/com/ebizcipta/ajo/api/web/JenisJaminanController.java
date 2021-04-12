package com.ebizcipta.ajo.api.web;

import com.ebizcipta.ajo.api.domain.JenisJaminan;
import com.ebizcipta.ajo.api.service.JenisJaminanService;
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
@Api(value="Master Data Jenis Jaminan")
public class JenisJaminanController {

    private final JenisJaminanService jenisJaminanService;
    private final AuditTrailUtil auditTrailUtil;

    public JenisJaminanController(JenisJaminanService jenisJaminanService, AuditTrailUtil auditTrailUtil) {
        this.jenisJaminanService = jenisJaminanService;
        this.auditTrailUtil = auditTrailUtil;
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Manual Data Transfer_READ,Master Data - Jenis Jaminan_READ')")
    @GetMapping("/jaminan")
    @ApiOperation("get All Jenis Jaminan")
    public ResponseEntity<List<JenisJaminanViewDTO>> getAllJenisJaminan(@RequestParam(value = "status", required = false) String status) {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Jenis Jaminan", "getAllJenisJaminan");
        final List<JenisJaminanViewDTO> result = jenisJaminanService.findAllJenisJaminan(status);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Jenis Jaminan", "getAllJenisJaminan");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Master Data -  Alamat Bank Penerbit_CREATE')")
    @PostMapping("/jaminan")
    @ApiOperation("Add Jenis Jaminan")
    public ResponseEntity<Boolean> addJenisJaminan(@Valid @RequestBody JenisJaminanDTO jenisJaminanDTO) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Jenis Jaminan", "addJenisJaminan");
        Boolean result = jenisJaminanService.saveJenisJaminan(jenisJaminanDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Jenis Jaminan", "addJenisJaminan");
        return ResponseEntity.created(new URI("/api/v1/jaminan"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Master Data -  Alamat Bank Penerbit_UPDATE')")
    @PutMapping("/jaminan")
    @ApiOperation("Update Jenis Jaminan")
    public ResponseEntity<Boolean> updateJenisJaminan(@Valid @RequestBody JenisJaminanDTO jenisJaminanDTO) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Jenis Jaminan", "updateJenisJaminan");
        Boolean result = jenisJaminanService.saveJenisJaminan(jenisJaminanDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Jenis Jaminan", "updateJenisJaminan");
        return ResponseEntity.created(new URI("/api/v1/jaminan"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Manual Data Transfer_READ,Master Data - Jenis Jaminan_READ')")
    @GetMapping("/jaminan/{id}")
    @ApiOperation("Get Detail jaminan")
    public ResponseEntity<Optional<JenisJaminanViewDTO>> getJaminanById(@PathVariable Long id){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Jenis Jaminan", "getJaminanById");
        final Optional<JenisJaminanViewDTO> result = jenisJaminanService.findJenisJaminanById(id);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Jenis Jaminan", "getJaminanById");
        return ResponseEntity.ok().body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Master Data -  Alamat Bank Penerbit_APPROVE')")
    @PostMapping("/jaminan/approval")
    @ApiOperation("Approval jaminan")
    public ResponseEntity<Boolean> approvalJaminan(@Valid @RequestBody GlobalApprovalDTO globalApprovalDTO) throws URISyntaxException{
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Jenis Jaminan", "approvalJaminan");
        Boolean result = jenisJaminanService.approvalJenisJaminan(globalApprovalDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Jenis Jaminan", "approvalJaminan");
        return ResponseEntity.created(new URI("/api/v1/jaminan"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Master Data -  Alamat Bank Penerbit_DELETE')")
    @DeleteMapping("/jaminan")
    @ApiOperation("Delete jaminan")
    public ResponseEntity<Boolean> deleteJaminan(@Valid @RequestBody List<IdDTO> idDTO){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Jenis Jaminan", "deleteJaminan");
        Boolean result = jenisJaminanService.deleteJenisJaminan(idDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Jenis Jaminan", "deleteJaminan");
        return ResponseEntity.ok().body(result);
    }
}
