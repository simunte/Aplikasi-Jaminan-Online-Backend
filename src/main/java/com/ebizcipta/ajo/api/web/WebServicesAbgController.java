package com.ebizcipta.ajo.api.web;

import com.ebizcipta.ajo.api.service.WebServicesAbgService;
import com.ebizcipta.ajo.api.service.dto.GlobalApprovalDTO;
import com.ebizcipta.ajo.api.service.dto.IdDTO;
import com.ebizcipta.ajo.api.service.dto.WebServicesAbgDTO;
import com.ebizcipta.ajo.api.service.dto.WebServicesAbgViewDTO;
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
@Api(value="Master Data Web Services Abg")
public class WebServicesAbgController {

    private final WebServicesAbgService webServicesAbgService;
    private final AuditTrailUtil auditTrailUtil;

    public WebServicesAbgController(WebServicesAbgService webServicesAbgService, AuditTrailUtil auditTrailUtil) {
        this.webServicesAbgService = webServicesAbgService;
        this.auditTrailUtil = auditTrailUtil;
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Manual Data Transfer_READ')")
    @GetMapping("/web/services")
    @ApiOperation("get All Web Services Abg")
    public ResponseEntity<List<WebServicesAbgViewDTO>> getAllWebServicesAbg(@RequestParam(value = "status", required = false) String status) {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Web Services Abg", "getAllWebServicesAbg");
        final List<WebServicesAbgViewDTO> result = webServicesAbgService.findAllWebServicesAbg(status);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Web Services Abg", "getAllWebServicesAbg");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Manual Data Transfer_CREATE,Manual Data Transfer_UPDATE')")
    @PostMapping("/web/services")
    @ApiOperation("Add Web Services Abg")
    public ResponseEntity<Boolean> addWebServicesAbg(@Valid @RequestBody WebServicesAbgDTO webServicesAbgDTO) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Web Services Abg", "addWebServicesAbg");
        Boolean result = webServicesAbgService.saveWebServicesAbg(webServicesAbgDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Web Services Abg", "addWebServicesAbg");
        return ResponseEntity.created(new URI("/api/v1/web/services"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Manual Data Transfer_CREATE' , 'Manual Data Transfer_UPDATE')")
    @PutMapping("/web/services")
    @ApiOperation("Update Web Services Abg")
    public ResponseEntity<Boolean> updateWebServicesAbg(@Valid @RequestBody WebServicesAbgDTO webServicesAbgDTO) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Web Services Abg", "updateWebServicesAbg");
        Boolean result = webServicesAbgService.saveWebServicesAbg(webServicesAbgDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Web Services Abg", "updateWebServicesAbg");
        return ResponseEntity.created(new URI("/api/v1/web/services"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Manual Data Transfer_READ')")
    @GetMapping("/web/services/{id}")
    @ApiOperation("Get Web Services Abg")
    public ResponseEntity<Optional<WebServicesAbgViewDTO>> getWebServicesAbgById(@PathVariable Long id){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Web Services Abg", "getWebServicesAbgById");
        final Optional<WebServicesAbgViewDTO> result = webServicesAbgService.findWebServicesAbgById(id);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Web Services Abg", "getWebServicesAbgById");
        return ResponseEntity.ok().body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Manual Data Transfer_APPROVE')")
    @GetMapping("/web/services/approval")
    @ApiOperation("Approval Web Services Abg")
    public ResponseEntity<Boolean> approvalWebServicesAbg(@Valid @RequestBody GlobalApprovalDTO globalApprovalDTO) throws URISyntaxException{
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Web Services Abg", "approvalWebServicesAbg");
        Boolean result = webServicesAbgService.approvalWebServicesAbg(globalApprovalDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Web Services Abg", "approvalWebServicesAbg");
        return ResponseEntity.created(new URI("/api/v1/web/services"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Manual Data Transfer_DELETE')")
    @DeleteMapping("/web/services")
    @ApiOperation("Delete Web Services Abg")
    public ResponseEntity<Boolean> deleteWebServicesAbg(@Valid @RequestBody List<IdDTO> idDTO){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Master Data Web Services Abg", "deleteWebServicesAbg");
        Boolean result = webServicesAbgService.deleteWebServicesAbg(idDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Master Data Web Services Abg", "deleteWebServicesAbg");
        return ResponseEntity.ok().body(result);
    }
}
