package com.ebizcipta.ajo.api.web;

import com.ebizcipta.ajo.api.service.JenisProdukService;
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
@Api(value="Master Data Jenis Produk")
public class JenisProdukController {

    private final JenisProdukService jenisProdukService;
    private final AuditTrailUtil auditTrailUtil;

    public JenisProdukController(JenisProdukService jenisProdukService, AuditTrailUtil auditTrailUtil) {
        this.jenisProdukService = jenisProdukService;
        this.auditTrailUtil = auditTrailUtil;
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Manual Data Transfer_READ,Master Data - Jenis Produk_READ')")
    @GetMapping("/produk")
    @ApiOperation("get All Jenis Produk")
    public ResponseEntity<List<JenisProdukViewDTO>> getAllJenisProduk(@RequestParam(value = "status", required = false) String status) {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Jenis Produk", "getAllJenisProduk");
        final List<JenisProdukViewDTO> result = jenisProdukService.findAllJenisProduk(status);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Jenis Produk", "getAllJenisProduk");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Master Data - Jenis Produk_CREATE')")
    @PostMapping("/produk")
    @ApiOperation("Add Jenis Produk")
    public ResponseEntity<Boolean> addJenisJenisProduk(@Valid @RequestBody JenisProdukDTO jenisProdukDTO) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Jenis Produk", "addJenisJenisProduk");
        Boolean result = jenisProdukService.saveJenisProduk(jenisProdukDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Jenis Produk", "addJenisJenisProduk");
        return ResponseEntity.created(new URI("/api/v1/produk"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Master Data - Jenis Produk_UPDATE')")
    @PutMapping("/produk")
    @ApiOperation("Update Jenis Produk")
    public ResponseEntity<Boolean> updateJenisProduk(@Valid @RequestBody JenisProdukDTO jenisProdukDTO) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Jenis Produk", "updateJenisProduk");
        Boolean result = jenisProdukService.saveJenisProduk(jenisProdukDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Jenis Produk", "updateJenisProduk");
        return ResponseEntity.created(new URI("/api/v1/produk"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Manual Data Transfer_READ,Master Data - Jenis Produk_READ')")
    @GetMapping("/produk/{id}")
    @ApiOperation("Get Detail produk")
    public ResponseEntity<Optional<JenisProdukViewDTO>> getProdukById(@PathVariable Long id){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Jenis Produk", "getProdukById");
        final Optional<JenisProdukViewDTO> result = jenisProdukService.findJenisProdukById(id);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Jenis Produk", "getProdukById");
        return ResponseEntity.ok().body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Master Data - Jenis Produk_APPROVE')")
    @PostMapping("/produk/approval")
    @ApiOperation("Approval produk")
    public ResponseEntity<Boolean> approvalProduk(@Valid @RequestBody GlobalApprovalDTO globalApprovalDTO) throws URISyntaxException{
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Jenis Produk", "approvalProduk");
        Boolean result = jenisProdukService.approvalJenisProduk(globalApprovalDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Jenis Produk", "approvalProduk");
        return ResponseEntity.created(new URI("/api/v1/produk"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Master Data - Jenis Produk_DELETE')")
    @DeleteMapping("/produk")
    @ApiOperation("Delete produk")
    public ResponseEntity<Boolean> deleteProduk(@Valid @RequestBody List<IdDTO> idDTO){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Jenis Produk", "deleteProduk");
        Boolean result = jenisProdukService.deleteJenisProduk(idDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Jenis Produk", "deleteProduk");
        return ResponseEntity.ok().body(result);
    }
}
