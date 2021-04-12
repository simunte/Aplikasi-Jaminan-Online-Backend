package com.ebizcipta.ajo.api.service;

import com.ebizcipta.ajo.api.service.dto.UraianPekerjaanDTO;

import java.util.LinkedList;
import java.util.List;

public interface UraianPekerjaanService {
    List<UraianPekerjaanDTO> findAllUraianPekerjaan();
    Boolean saveUraianPekerjaan(UraianPekerjaanDTO uraianPekerjaanDTO);
}
