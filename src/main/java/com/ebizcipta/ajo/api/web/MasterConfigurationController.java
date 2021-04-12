package com.ebizcipta.ajo.api.web;

import com.ebizcipta.ajo.api.service.MasterConfigurationService;
import com.ebizcipta.ajo.api.service.dto.GlobalApprovalDTO;
import com.ebizcipta.ajo.api.service.dto.IdDTO;
import com.ebizcipta.ajo.api.service.dto.MasterConfigurationDTO;
import com.ebizcipta.ajo.api.service.dto.MasterConfigurationViewDTO;
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
@Api(value="Master Configuration Data")
public class MasterConfigurationController {
    private final MasterConfigurationService masterConfigurationService;
    private final AuditTrailUtil auditTrailUtil;

    public MasterConfigurationController(MasterConfigurationService masterConfigurationService, AuditTrailUtil auditTrailUtil) {
        this.masterConfigurationService = masterConfigurationService;
        this.auditTrailUtil = auditTrailUtil;
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Configuration - Master Configuration_READ,User Access Management - User Access_READ')")
    @GetMapping("/master/configuration")
    @ApiOperation("get All Master Configuration Data")
    public ResponseEntity<List<MasterConfigurationViewDTO>> getAllMasterConfiguration(@RequestParam(value = "status", required = false) String status) {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Configuration Data", "getAllMasterConfiguration");
        final List<MasterConfigurationViewDTO> result = masterConfigurationService.findAllMasterConfiguration(status);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Configuration Data", "getAllMasterConfiguration");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Configuration - Master Configuration_CREATE')")
    @PostMapping("/master/configuration")
    @ApiOperation("Add Master Configuration Data")
    public ResponseEntity<Boolean> addMasterConfiguration(@Valid @RequestBody MasterConfigurationDTO masterConfigurationDTO) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Configuration Data", "addMasterConfiguration");
        Boolean result = masterConfigurationService.saveMasterConfiguration(masterConfigurationDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Configuration Data", "addMasterConfiguration");
        return ResponseEntity.created(new URI("/api/v1/master/configuration"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Configuration - Master Configuration_UPDATE')")
    @PutMapping("/master/configuration")
    @ApiOperation("Update Master Configuration Data")
    public ResponseEntity<Boolean> updateMasterConfiguration(@Valid @RequestBody MasterConfigurationDTO masterConfigurationDTO) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Configuration Data", "updateMasterConfiguration");
        Boolean result = masterConfigurationService.saveMasterConfiguration(masterConfigurationDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Configuration Data", "updateMasterConfiguration");
        return ResponseEntity.created(new URI("/api/v1/master/configuration"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Configuration - Master Configuration_READ,User Access Management - User Access_READ')")
    @GetMapping("/master/configuration/{id}")
    @ApiOperation("Get Detail Master Configuration Data")
    public ResponseEntity<Optional<MasterConfigurationViewDTO>> getMasterConfigurationById(@PathVariable Long id){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Configuration Data", "getMasterConfigurationById");
        final Optional<MasterConfigurationViewDTO> result = masterConfigurationService.findMasterConfigurationById(id);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Configuration Data", "getMasterConfigurationById");
        return ResponseEntity.ok().body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Configuration - Master Configuration_CREATE')")
    @PostMapping("/master/configuration/approval")
    @ApiOperation("Approval Master Configuration Data")
    public ResponseEntity<Boolean> approvalMasterConfiguration(@Valid @RequestBody GlobalApprovalDTO globalApprovalDTO) throws URISyntaxException{
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Configuration Data", "approvalMasterConfiguration");
        Boolean result = masterConfigurationService.approvalMasterConfiguration(globalApprovalDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Configuration Data", "approvalMasterConfiguration");
        return ResponseEntity.created(new URI("/api/v1/jaminan"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Configuration - Master Configuration_DELETE')")
    @DeleteMapping("/master/configuration")
    @ApiOperation("Delete Master Configuration Data")
    public ResponseEntity<Boolean> deleteMasterConfiguration(@Valid @RequestBody List<IdDTO> idDTO){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Configuration Data", "deleteMasterConfiguration");
        Boolean result = masterConfigurationService.deleteMasterConfiguration(idDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Configuration Data", "deleteMasterConfiguration");
        return ResponseEntity.ok().body(result);
    }
}
