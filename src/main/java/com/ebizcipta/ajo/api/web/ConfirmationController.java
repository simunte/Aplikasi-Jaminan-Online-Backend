package com.ebizcipta.ajo.api.web;

import com.ebizcipta.ajo.api.service.ConfirmationService;
import com.ebizcipta.ajo.api.service.FileManagementService;
import com.ebizcipta.ajo.api.service.dto.ConfirmationDTO;
import com.ebizcipta.ajo.api.service.dto.ViewConfirmationDTO;
import com.ebizcipta.ajo.api.util.AuditTrailUtil;
import com.ebizcipta.ajo.api.util.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Api(value="Bank Guarantee Confirmation")
public class ConfirmationController {
    private final ConfirmationService confirmationService;
    private final FileManagementService fileManagementService;
    private final AuditTrailUtil auditTrailUtil;

    public ConfirmationController(ConfirmationService confirmationService, FileManagementService fileManagementService, AuditTrailUtil auditTrailUtil) {
        this.confirmationService = confirmationService;
        this.fileManagementService = fileManagementService;
        this.auditTrailUtil = auditTrailUtil;
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Manual Data Transfer_READ')")
    @GetMapping("/confirmation")
    @ApiOperation("get All BG Confirmation")
    public ResponseEntity<List<ViewConfirmationDTO>> getAllBgConfirmation() {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Bank Guarantee Confirmation", "getAllBgConfirmation");
        final List<ViewConfirmationDTO> result = confirmationService.findAllConfirmation();
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Bank Guarantee Confirmation", "getAllBgConfirmation");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Manual Data Transfer_READ')")
    @GetMapping("/confirmation/{idJaminan}")
    @ApiOperation("get BG Confirmation By Nomor Jaminan")
    public ResponseEntity<Optional<ViewConfirmationDTO>> getBgConfirmationByNomorJaminan(@PathVariable Long idJaminan){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Bank Guarantee Confirmation", "getBgConfirmationByNomorJaminan");
        final Optional<ViewConfirmationDTO> result = confirmationService.findConfirmationByNomorJaminan(idJaminan);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Bank Guarantee Confirmation", "getBgConfirmationByNomorJaminan");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_CREATE,Bank Guarantee_UPDATE,Bank Guarantee_APPROVE')")
    @PostMapping("/confirmation")
    @ApiOperation("Add BG Confirmation")
    public ResponseEntity<Boolean> addJenisBgConfirmation(@Valid @RequestBody ConfirmationDTO confirmationDTO,
                                                          @RequestParam(value = "draft", defaultValue = "false") Boolean draft) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Bank Guarantee Confirmation", "addJenisBgConfirmation");
        Boolean result = confirmationService.saveConfirmation(confirmationDTO, draft.toString());
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Bank Guarantee Confirmation", "addJenisBgConfirmation");
        return ResponseEntity.created(new URI("/api/v1/confirmation"))
                .body(result);
    }
}
