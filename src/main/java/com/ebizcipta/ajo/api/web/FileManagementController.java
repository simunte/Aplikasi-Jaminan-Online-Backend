package com.ebizcipta.ajo.api.web;

import com.ebizcipta.ajo.api.service.FileManagementService;
import com.ebizcipta.ajo.api.service.dto.FileManagementDTO;
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
import java.io.IOException;


@Slf4j
@RestController
@RequestMapping("/api/v1")
@Api(value="File Management")
public class FileManagementController {

    private final FileManagementService fileManagementService;
    private final AuditTrailUtil auditTrailUtil;

    public FileManagementController(FileManagementService fileManagementService, AuditTrailUtil auditTrailUtil) {
        this.fileManagementService = fileManagementService;
        this.auditTrailUtil = auditTrailUtil;
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Manual Data Transfer_READ,User Access Management - User Access_READ')")
    @PostMapping("/file/upload")
    @ApiOperation("upload File")
    public ResponseEntity<FileManagementDTO> uploadFile(MultipartFile file ,
                                                        @RequestParam (value =  "type", defaultValue = "pdf")String type,
                                                        @RequestParam (value =  "idJaminan", required = false) Long idJaminan,
                                                        @RequestParam (value = "codeJaminan", required = false) String codeJaminan,
                                                        @RequestParam (value = "tanggalTerbit", required = false) Long tanggalTerbit){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "File Management", "uploadFile");
        FileManagementDTO fileManagementDTO = fileManagementService.uploadFile(file,type, idJaminan, codeJaminan, tanggalTerbit);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "File Management", "uploadFile");
        return ResponseEntity.ok()
                .body(fileManagementDTO);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ')")
    @PostMapping("/dokumen/pendukung")
    @ApiOperation("upload Supported File")
    public ResponseEntity<FileManagementDTO> uploadFileSupported(MultipartFile file){
        FileManagementDTO fileManagementDTO = fileManagementService.uploadSupportDocument(file);
        return ResponseEntity.ok()
                .body(fileManagementDTO);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Manual Data Transfer_READ,User Access Management - User Access_READ')")
    @GetMapping("/file/download")
    @ApiOperation("download File")
    public void downloadFile(HttpServletResponse response,
                             @RequestParam("fileName") String fileName) throws IOException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "File Management", "downloadFile");
        fileManagementService.downloadFile(response, fileName);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "File Management", "downloadFile");
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ')")
    @GetMapping("/file/download/nasabah")
    @ApiOperation("download File Nasabah")
    public void downloadFileNasabah(HttpServletResponse response,
                             @RequestParam("fileUrl") String fileUrl) throws IOException {
        fileManagementService.downloadFileNasabah(response, fileUrl);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Manual Data Transfer_READ,User Access Management - User Access_READ')")
    @GetMapping("/file/download-only")
    @ApiOperation("download File only")
    public void downloadFileOnly(HttpServletResponse response,
                             @RequestParam("fileName") String fileName) throws IOException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "File Management", "downloadFileOnly");
        fileManagementService.downloadFileOnly(response, fileName);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "File Management", "downloadFileOnly");
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Bank Guarantee_READ,Report - Recapitulation_READ,Report - Verification_READ,Report - Settlement_READ,Manual Data Transfer_READ,User Access Management - User Access_READ')")
    @GetMapping("/file/export/confirmation")
    @ApiOperation(value = "Export Surat Konfirmasi")
    public void exportSuratKonfirmasi(HttpServletResponse response,
                                      @RequestParam("idJaminan") Long idJaminan,
                                      @RequestParam(value = "ttdFile", defaultValue = "true") Boolean ttdFile
    ) throws IOException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "File Management", "exportSuratKonfirmasi");
        fileManagementService.downloadSuratConfirmasi(response, idJaminan, ttdFile);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "File Management", "exportSuratKonfirmasi");
    }
}
