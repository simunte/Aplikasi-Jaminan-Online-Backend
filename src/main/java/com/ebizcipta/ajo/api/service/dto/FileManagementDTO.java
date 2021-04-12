package com.ebizcipta.ajo.api.service.dto;

import lombok.Data;

@Data
public class FileManagementDTO {
    private String inputFileName;
    private String originalFileName;
    private String newFileName;
    private String fileSaveUrl;
}
