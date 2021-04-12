package com.ebizcipta.ajo.api.service.impl;

import com.ebizcipta.ajo.api.domain.UraianPekerjaan;
import com.ebizcipta.ajo.api.repositories.UraianPekerjaanRepository;
import com.ebizcipta.ajo.api.service.UraianPekerjaanService;
import com.ebizcipta.ajo.api.service.dto.UraianPekerjaanDTO;
import com.ebizcipta.ajo.api.service.mapper.UraianPekerjaanMapper;
import com.ebizcipta.ajo.api.util.AuditTrailUtil;
import com.ebizcipta.ajo.api.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class UraianPekerjaanServiceImpl implements UraianPekerjaanService {
    @Autowired
    private UraianPekerjaanRepository uraianPekerjaanRepository;

    @Autowired
    private AuditTrailUtil auditTrailUtil;

    @Override
    @Transactional(readOnly = true)
    public List<UraianPekerjaanDTO> findAllUraianPekerjaan() {
        return uraianPekerjaanRepository.findAll().stream()
                .map(uraianPekerjaan -> UraianPekerjaanMapper.INSTANCE.toDto(uraianPekerjaan, new UraianPekerjaanDTO()))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional
    public Boolean saveUraianPekerjaan(UraianPekerjaanDTO uraianPekerjaanDTO) {
        UraianPekerjaan uraianPekerjaan = new UraianPekerjaan();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        if (uraianPekerjaanDTO.getId()!=null){
            UraianPekerjaan uraianPekerjaanFound = uraianPekerjaanRepository.findById(uraianPekerjaanDTO.getId())
                    .orElseThrow(()->new EntityNotFoundException("Uraian Pekerjaan Not Found"));
            UraianPekerjaan oldUraian = (UraianPekerjaan) SerializationUtils.clone(uraianPekerjaanFound);
            uraianPekerjaan = UraianPekerjaanMapper.INSTANCE.toEntity(uraianPekerjaanDTO, uraianPekerjaanFound);

            //TODO AUDIT TRAIL UPDATE URAIAN PEKERJAAN --DONE
            auditTrailUtil.saveAudit(Constants.Event.UPDATE, Constants.Module.URAIAN_PEKERJAAN,new JSONObject(oldUraian).toString(), new JSONObject(uraianPekerjaan).toString(), Constants.Remark.UPDATE_URAIAN_PEKERJAAN, userLogin );
        }else {
            uraianPekerjaan = UraianPekerjaanMapper.INSTANCE.toEntity(uraianPekerjaanDTO, uraianPekerjaan);

            //TODO AUDIT TRAIL INSERT DATA BARU URAIAN PEKERJAAN --DONE
            auditTrailUtil.saveAudit(Constants.Event.CREATE, Constants.Module.URAIAN_PEKERJAAN,null, new JSONObject(uraianPekerjaan).toString(), Constants.Remark.CREATE_URAIAN_PEKERJAAN, userLogin );
        }
        uraianPekerjaanRepository.save(uraianPekerjaan);
        return Boolean.TRUE;
    }
}
