package com.ebizcipta.ajo.api.service;

import com.ebizcipta.ajo.api.service.dto.FileManagementDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface FileManagementService {
    FileManagementDTO uploadFile(MultipartFile file, String type, Long idJaminan, String codeJaminan, Long tanggalTerbit);
    void downloadFile(HttpServletResponse response, String fileName) throws IOException;
//    Map<String, Object> exportSuratKonfirmasi(String nomorJaminan);
    void downloadSuratConfirmasi(HttpServletResponse response, Long idJaminan, Boolean ttdFile) throws IOException;
    void downloadFileOnly(HttpServletResponse response, String filePathName) throws IOException;
}
