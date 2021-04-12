package com.ebizcipta.ajo.api.web;

import com.ebizcipta.ajo.api.service.ColumnUserService;
import com.ebizcipta.ajo.api.service.dto.ColumnUserDTO;
import com.ebizcipta.ajo.api.service.dto.CurrencyDTO;
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
@Api(value="Column User Data")
public class ColumnUserController {
    private final ColumnUserService columnUserService;
    private final AuditTrailUtil auditTrailUtil;

    public ColumnUserController(ColumnUserService columnUserService, AuditTrailUtil auditTrailUtil) {
        this.columnUserService = columnUserService;
        this.auditTrailUtil = auditTrailUtil;
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Manual Data Transfer_READ,User Access Management - User Access_READ')")
    @PostMapping("/column")
    @ApiOperation("Add Column")
    public ResponseEntity<Boolean> addColumn(@Valid @RequestBody ColumnUserDTO columnUserDTO) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Column User Data", "addColumn");
        Boolean result = columnUserService.saveColumnUser(columnUserDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Column User Data", "addColumn");
        return ResponseEntity.created(new URI("/api/v1/column"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Manual Data Transfer_READ,User Access Management - User Access_READ')")
    @PutMapping("/column")
    @ApiOperation("Update Column")
    public ResponseEntity<Boolean> updateColumn(@Valid @RequestBody ColumnUserDTO columnUserDTO) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Column User Data", "updateColumn");
        Boolean result = columnUserService.saveColumnUser(columnUserDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Column User Data", "updateColumn");
        return ResponseEntity.created(new URI("/api/v1/column"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Manual Data Transfer_READ,User Access Management - User Access_READ')")
    @GetMapping("/column")
    @ApiOperation("get All Column")
    public ResponseEntity<List<ColumnUserDTO>> getAllColumn() {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Column User Data", "getAllColumn");
        final List<ColumnUserDTO> result = columnUserService.findAllColumnUser();
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Column User Data", "getAllColumn");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Manual Data Transfer_READ,User Access Management - User Access_READ')")
    @GetMapping("/column/detail")
    @ApiOperation("get Detail Column")
    public ResponseEntity<Optional<ColumnUserDTO>> getDetailColumn() {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Column User Data", "getDetailColumn");
        final Optional<ColumnUserDTO> result = columnUserService.findByUsername();
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Column User Data", "getDetailColumn");
        return ResponseEntity.ok()
                .body(result);
    }
}
