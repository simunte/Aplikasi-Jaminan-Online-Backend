package com.ebizcipta.ajo.api.web;

import com.ebizcipta.ajo.api.service.CurrencyService;
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
@Api(value="Master Data Currency")
public class CurrencyController {

    private final CurrencyService currencyService;
    private final AuditTrailUtil auditTrailUtil;

    public CurrencyController(CurrencyService currencyService, AuditTrailUtil auditTrailUtil) {
        this.currencyService = currencyService;
        this.auditTrailUtil = auditTrailUtil;
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Manual Data Transfer_READ,Master Data - Currency_READ')")
    @GetMapping("/currency")
    @ApiOperation("get All Currency")
    public ResponseEntity<List<CurrencyViewDTO>> getAllCurrency(@RequestParam(value = "status", required = false) String status) {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Currency", "getAllCurrency");
        final List<CurrencyViewDTO> result = currencyService.findAllCurrency(status);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Currency", "getAllCurrency");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Master Data - Currency_CREATE')")
    @PostMapping("/currency")
    @ApiOperation("Add Currency")
    public ResponseEntity<Boolean> addCurrency(@Valid @RequestBody CurrencyDTO currencyDTO) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Currency", "addCurrency");
        Boolean result = currencyService.saveCurrency(currencyDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Currency", "addCurrency");
        return ResponseEntity.created(new URI("/api/v1/currency"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Master Data - Currency_UPDATE')")
    @PutMapping("/currency")
    @ApiOperation("Update Currency")
    public ResponseEntity<Boolean> updateCurrency(@Valid @RequestBody CurrencyDTO currencyDTO) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Currency", "updateCurrency");
        Boolean result = currencyService.saveCurrency(currencyDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Currency", "updateCurrency");
        return ResponseEntity.created(new URI("/api/v1/currency"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Manual Data Transfer_READ,Master Data - Currency_READ')")
    @GetMapping("/currency/{id}")
    @ApiOperation("Get Detail currency")
    public ResponseEntity<Optional<CurrencyViewDTO>> getCurrencyById(@PathVariable Long id){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Currency", "getCurrencyById");
        final Optional<CurrencyViewDTO> result = currencyService.findCurrencyById(id);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Currency", "getCurrencyById");
        return ResponseEntity.ok().body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Master Data - Currency_APPROVE')")
    @PostMapping("/currency/approval")
    @ApiOperation("Approval currency")
    public ResponseEntity<Boolean> approvalCurrency(@Valid @RequestBody GlobalApprovalDTO globalApprovalDTO) throws URISyntaxException{
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Currency", "approvalCurrency");
        Boolean result = currencyService.approvalCurrency(globalApprovalDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Currency", "approvalCurrency");
        return ResponseEntity.created(new URI("/api/v1/currency"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Master Data - Currency_DELETE')")
    @DeleteMapping("/currency")
    @ApiOperation("Delete currency")
    public ResponseEntity<Boolean> deleteCurrency(@Valid @RequestBody List<IdDTO> idDTO){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Currency", "deleteCurrency");
        Boolean result = currencyService.deleteCurrency(idDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Currency", "deleteCurrency");
        return ResponseEntity.ok().body(result);
    }
}
