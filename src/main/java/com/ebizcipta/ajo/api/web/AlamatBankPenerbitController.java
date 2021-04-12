package com.ebizcipta.ajo.api.web;

import com.ebizcipta.ajo.api.service.AlamatBankPenerbitService;
import com.ebizcipta.ajo.api.service.dto.AlamatBankPenerbitDTO;
import com.ebizcipta.ajo.api.service.dto.AlamatBankPenerbitViewDTO;
import com.ebizcipta.ajo.api.service.dto.GlobalApprovalDTO;
import com.ebizcipta.ajo.api.service.dto.IdDTO;
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
@Api(value="Master Data Alamat Bank Penerbit")
public class AlamatBankPenerbitController {
    private final AlamatBankPenerbitService alamatBankPenerbitService;
    private final AuditTrailUtil auditTrailUtil;

    public AlamatBankPenerbitController(AlamatBankPenerbitService alamatBankPenerbitService, AuditTrailUtil auditTrailUtil) {
        this.alamatBankPenerbitService = alamatBankPenerbitService;
        this.auditTrailUtil = auditTrailUtil;
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Manual Data Transfer_READ,Master Data -  Alamat Bank Penerbit_READ')")
    @GetMapping("/alamat/bank/penerbit")
    @ApiOperation("get All Alamat Bank Penerbit")
    public ResponseEntity<List<AlamatBankPenerbitViewDTO>> getAllAlamatBankPenerbit(@RequestParam(value = "status", required = false) String status) {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Alamat Bank Penerbit", "getAllAlamatBankPenerbit");
        final List<AlamatBankPenerbitViewDTO> result = alamatBankPenerbitService.findAllAlamatBankPenerbit(status);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Alamat Bank Penerbit", "getAllAlamatBankPenerbit");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Master Data -  Alamat Bank Penerbit_CREATE,Master Data -  Alamat Bank Penerbit_UPDATE')")
    @PostMapping("/alamat/bank/penerbit")
    @ApiOperation("Add Alamat Bank Penerbit")
    public ResponseEntity<Boolean> addAlamatBankPenerbit(@Valid @RequestBody AlamatBankPenerbitDTO alamatBankPenerbitDTO) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Alamat Bank Penerbit", "addAlamatBankPenerbit");
        Boolean result = alamatBankPenerbitService.saveAlamatBankPenerbit(alamatBankPenerbitDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Alamat Bank Penerbit", "addAlamatBankPenerbit");
        return ResponseEntity.created(new URI("/api/v1/alamat/bank/penerbit"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Master Data -  Alamat Bank Penerbit_CREATE,Master Data -  Alamat Bank Penerbit_UPDATE')")
    @PutMapping("/alamat/bank/penerbit")
    @ApiOperation("Update Alamat Bank Penerbit")
    public ResponseEntity<Boolean> updateAlamatBankPenerbit(@Valid @RequestBody AlamatBankPenerbitDTO alamatBankPenerbitDTO) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Alamat Bank Penerbit", "updateAlamatBankPenerbit");
        Boolean result = alamatBankPenerbitService.saveAlamatBankPenerbit(alamatBankPenerbitDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Alamat Bank Penerbit", "updateAlamatBankPenerbit");
        return ResponseEntity.created(new URI("/api/v1/alamat/bank/penerbit"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Manual Data Transfer_READ,Master Data -  Alamat Bank Penerbit_READ')")
    @GetMapping("/alamat/bank/penerbit/{id}")
    @ApiOperation("Get Detail Alamat Bank Penerbit")
    public ResponseEntity<Optional<AlamatBankPenerbitViewDTO>> getAlamatBankPenerbitById(@PathVariable Long id){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Alamat Bank Penerbit", "getAlamatBankPenerbitById");
        final Optional<AlamatBankPenerbitViewDTO> result = alamatBankPenerbitService.findAlamatBankPenerbitById(id);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Alamat Bank Penerbit", "getAlamatBankPenerbitById");
        return ResponseEntity.ok().body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Master Data -  Alamat Bank Penerbit_APPROVE')")
    @PostMapping("/alamat/bank/penerbit/approval")
    @ApiOperation("Approval Alamat Bank Penerbit")
    public ResponseEntity<Boolean> approvalAlamatBankPenerbit(@Valid @RequestBody GlobalApprovalDTO globalApprovalDTO){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Alamat Bank Penerbit", "approvalAlamatBankPenerbit");
        Boolean result = alamatBankPenerbitService.approvalAlamatBankPenerbit(globalApprovalDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Alamat Bank Penerbit", "approvalAlamatBankPenerbit");
        return ResponseEntity.ok().body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Master Data -  Alamat Bank Penerbit_DELETE')")
    @DeleteMapping("/alamat/bank/penerbit")
    @ApiOperation("Delete Alamat Bank Penerbit")
    public ResponseEntity<Boolean> deleteAlamatBankPenerbit(@Valid @RequestBody List<IdDTO> idDTO){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Alamat Bank Penerbit", "deleteAlamatBankPenerbit");
        Boolean result = alamatBankPenerbitService.deleteAlamatBankPenerbit(idDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Alamat Bank Penerbit", "deleteAlamatBankPenerbit");
        return ResponseEntity.ok().body(result);
    }
}
