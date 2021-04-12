package com.ebizcipta.ajo.api.web;

import com.ebizcipta.ajo.api.service.FileManagementService;
import com.ebizcipta.ajo.api.service.RegistrationService;
import com.ebizcipta.ajo.api.service.dto.*;
import com.ebizcipta.ajo.api.util.AuditTrailUtil;
import com.ebizcipta.ajo.api.util.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Api(value="Bank Guarantee Registration")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final AuditTrailUtil auditTrailUtil;

    public RegistrationController(RegistrationService registrationService, AuditTrailUtil auditTrailUtil) {
        this.registrationService = registrationService;
        this.auditTrailUtil = auditTrailUtil;
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ')")
    @GetMapping("/registration")
    @ApiOperation("get All BG Registration")
    public ResponseEntity<List<ViewRegistrationDTO>> getAllBgRegistration(@RequestParam(value = "status", required = false) String status) {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Bank Guarantee Registration", "getAllBgRegistration");
        final List<ViewRegistrationDTO> result = registrationService.findAllBgRegistration(status);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Bank Guarantee Registration", "getAllBgRegistration");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ')")
    @GetMapping("/registration/{idJaminan}")
    @ApiOperation("get BG Registration By Nomor Jaminan")
    public ResponseEntity<Optional<ViewRegistrationDTO>> getBgRegistrationByNomorJaminan(@PathVariable Long idJaminan) {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Bank Guarantee Registration", "getBgRegistrationByNomorJaminan");
        final Optional<ViewRegistrationDTO> result = registrationService.findByNomorJaminan(idJaminan);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Bank Guarantee Registration", "getBgRegistrationByNomorJaminan");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ')")
    @GetMapping("/registration/detail/{idJaminan}")
    @ApiOperation("get BG Registration Detail By Nomor Jaminan")
    public ResponseEntity<Optional<BankGuaranteeDTO>> getBgRegistrationDetailByNomorJaminan(@PathVariable Long idJaminan) {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Bank Guarantee Registration", "getBgRegistrationDetailByNomorJaminan");
        final Optional<BankGuaranteeDTO> result = registrationService.getDetailBankGuarantee(idJaminan);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Bank Guarantee Registration", "getBgRegistrationDetailByNomorJaminan");
        return ResponseEntity.ok().body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ')")
    @GetMapping("/registration/bg-pln")
    @ApiOperation("get All BG Registration")
    public ResponseEntity<List<ViewRegistrationDTO>> getAllBgPLN(@RequestParam(value = "status", required = false) String status,
                                                                 @RequestParam(value = "menu", required = false) String menu) {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Bank Guarantee Registration", "getAllBgPLN");
        final List<ViewRegistrationDTO> result = registrationService.findAllBgPLN(status, menu);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Bank Guarantee Registration", "getAllBgPLN");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_CREATE,Bank Guarantee_UPDATE')")
    @PostMapping("/registration")
    @ApiOperation("Add BG Registration")
    public ResponseEntity<ConfirmationDTO> addJenisBgRegistration(@Valid @RequestBody RegistrationDTO registrationDTO) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Bank Guarantee Registration", "addJenisBgRegistration");
        ConfirmationDTO result = registrationService.saveRegistration(registrationDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Bank Guarantee Registration", "addJenisBgRegistration");
        return ResponseEntity.created(new URI("/api/v1/registration"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_CREATE,Bank Guarantee_UPDATE')")
    @PutMapping("/registration")
    @ApiOperation("Update BG Registration")
    public ResponseEntity<ConfirmationDTO> updateBgRegistration(@Valid @RequestBody RegistrationDTO registrationDTO) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Bank Guarantee Registration", "updateBgRegistration");
        ConfirmationDTO result = registrationService.saveRegistration(registrationDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Bank Guarantee Registration", "updateBgRegistration");
        return ResponseEntity.created(new URI("/api/v1/registration"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_APPROVE,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ')")
    @PostMapping("/registration/approval")
    @ApiOperation("Approval/Reject BG Registration")
    public ResponseEntity<Boolean> approvalBgRegistration(@Valid @RequestBody ApprovalDTO approvalDTO){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Bank Guarantee Registration", "approvalBgRegistration");
        Boolean result = registrationService.registrationApproval(approvalDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Bank Guarantee Registration", "approvalBgRegistration");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ')")
    @GetMapping("/registration/draft/{idJaminan}")
    @ApiOperation("Draft/WFA BG Registration")
    public ResponseEntity<Boolean> draftBgRegistration(
                                                    @PathVariable Long idJaminan,
                                                    @RequestParam(value = "draft") Boolean draft){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Bank Guarantee Registration", "draftBgRegistration");
        Boolean result = registrationService.submitDraft(idJaminan, draft);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Bank Guarantee Registration", "draftBgRegistration");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_DELETE')")
    @DeleteMapping("/registration")
    @ApiOperation("Delete Bank Guarantee")
    public ResponseEntity<Boolean> deleteBg(@Valid @RequestBody List<IdDTO> idDTO){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Bank Guarantee Registration", "deleteBg");
        Boolean result = registrationService.deleteBankGuarantee(idDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Bank Guarantee Registration", "deleteBg");
        return ResponseEntity.ok()
                .body(result);
    }
}
