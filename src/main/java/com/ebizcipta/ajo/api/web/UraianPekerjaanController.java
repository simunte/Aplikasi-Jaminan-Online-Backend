package com.ebizcipta.ajo.api.web;

import com.ebizcipta.ajo.api.service.UraianPekerjaanService;
import com.ebizcipta.ajo.api.service.dto.JenisProdukDTO;
import com.ebizcipta.ajo.api.service.dto.UraianPekerjaanDTO;
import com.ebizcipta.ajo.api.util.AuditTrailUtil;
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

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Api(value="Master Data Uraian Pekerjaan")
public class UraianPekerjaanController {
    private final UraianPekerjaanService uraianPekerjaanService;
    private final AuditTrailUtil auditTrailUtil;

    public UraianPekerjaanController(UraianPekerjaanService uraianPekerjaanService, AuditTrailUtil auditTrailUtil) {
        this.uraianPekerjaanService = uraianPekerjaanService;
        this.auditTrailUtil = auditTrailUtil;
    }

//    @GetMapping("/uraian/pekerjaan")
//    @ApiOperation("get All Uraian Pekerjaan")
//    public ResponseEntity<List<UraianPekerjaanDTO>> getAllUraianPekerjaan() {
//        final List<UraianPekerjaanDTO> result = uraianPekerjaanService.findAllUraianPekerjaan();
//        return ResponseEntity.ok()
//                .body(result);
//    }
//
//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("/uraian/pekerjaan")
//    @ApiOperation("Add Uraian Pekerjaan")
//    public ResponseEntity<Boolean> addUraianPekerjaan(@Valid @RequestBody UraianPekerjaanDTO uraianPekerjaanDTO) throws URISyntaxException {
//        Boolean result = uraianPekerjaanService.saveUraianPekerjaan(uraianPekerjaanDTO);
//        return ResponseEntity.created(new URI("/api/v1/uraian/pekerjaan"))
//                .body(result);
//    }
//
//    @PreAuthorize("isAuthenticated()")
//    @PutMapping("/uraian/pekerjaan")
//    @ApiOperation("Update Uraian Pekerjaan")
//    public ResponseEntity<Boolean> updateUraianPekerjaan(@Valid @RequestBody UraianPekerjaanDTO uraianPekerjaanDTO) throws URISyntaxException {
//        Boolean result = uraianPekerjaanService.saveUraianPekerjaan(uraianPekerjaanDTO);
//        return ResponseEntity.created(new URI("/api/v1/uraian/pekerjaan"))
//                .body(result);
//    }
}
