package com.ebizcipta.ajo.api.service.impl;

import com.ebizcipta.ajo.api.domain.Currency;
import com.ebizcipta.ajo.api.domain.JenisJaminan;
import com.ebizcipta.ajo.api.exception.AjoException;
import com.ebizcipta.ajo.api.exception.BadRequestAlertException;
import com.ebizcipta.ajo.api.repositories.JenisJaminanRepository;
import com.ebizcipta.ajo.api.service.JenisJaminanService;
import com.ebizcipta.ajo.api.service.dto.GlobalApprovalDTO;
import com.ebizcipta.ajo.api.service.dto.IdDTO;
import com.ebizcipta.ajo.api.service.dto.JenisJaminanDTO;
import com.ebizcipta.ajo.api.service.dto.JenisJaminanViewDTO;
import com.ebizcipta.ajo.api.service.mapper.JenisJaminanMapper;
import com.ebizcipta.ajo.api.util.AuditTrailUtil;
import com.ebizcipta.ajo.api.util.Constants;
import com.ebizcipta.ajo.api.util.Statusutil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class JenisJaminanServiceImpl implements JenisJaminanService{

    @Autowired
    JenisJaminanRepository jenisJaminanRepository;

    @Autowired
    private AuditTrailUtil auditTrailUtil;

    @Override
    @Transactional(readOnly = true)
    public List<JenisJaminanViewDTO> findAllJenisJaminan(String status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (status == null || status == "" || status.equalsIgnoreCase("null")){
            return jenisJaminanRepository.findByStatusNot(Constants.MasterDataStatus.INACTIVE).stream()
                    .map(jenisJaminan -> JenisJaminanMapper.INSTANCE.toDto(jenisJaminan, new JenisJaminanViewDTO()))
                    .collect(Collectors.toCollection(LinkedList::new));
        }else {
            return jenisJaminanRepository.findByStatusAndModifiedByNot(status, authentication.getName()).stream()
                    .map(jenisJaminan -> JenisJaminanMapper.INSTANCE.toDto(jenisJaminan, new JenisJaminanViewDTO()))
                    .collect(Collectors.toCollection(LinkedList::new));
        }
    }

    @Override
    @Transactional
    public Boolean saveJenisJaminan(JenisJaminanDTO jenisJaminanDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        JenisJaminan jenisJaminan = new JenisJaminan();
        JenisJaminan oldJenisJaminan = null;
        if (jenisJaminanDTO.getId() != null){
            JenisJaminan jenisJaminanFound = jenisJaminanRepository.findById(jenisJaminanDTO.getId())
                    .orElseThrow(()-> new EntityNotFoundException("Jenis Jaminan Not Found"));
            oldJenisJaminan  = (JenisJaminan) SerializationUtils.clone(jenisJaminanFound);
            jenisJaminan = JenisJaminanMapper.INSTANCE.toEntity(jenisJaminanDTO, jenisJaminanFound);
        }else {
            List<JenisJaminan> checkJenisJaminan = jenisJaminanRepository.findByCodeJaminanOrJenisJaminanEqualsIgnoreCase
                    (jenisJaminanDTO.getCodeJaminan(), jenisJaminanDTO.getJenisJaminan())
                    .stream()
                    .filter(x -> !Statusutil.getStatusForMasterDataUpdate().contains(x.getStatus()))
                    .collect(Collectors.toCollection(LinkedList::new));

            if(!checkJenisJaminan.isEmpty()){
                throw new AjoException("Kode / Jenis Jaminan tidak boleh sama");
            }
            jenisJaminan = JenisJaminanMapper.INSTANCE.toEntity(jenisJaminanDTO, jenisJaminan);
        }
        jenisJaminan.setStatus(Constants.MasterDataStatus.WAITING_FOR_APPROVAL);
        jenisJaminan.setActivated(Boolean.FALSE);
        jenisJaminanRepository.save(jenisJaminan);

        //TODO AUDIT TRAIL CREATE / UPDATE JENIS JAMINAN BUT WAITING APPROVAL --DONE
        auditTrailUtil.saveAudit(
                jenisJaminanDTO.getId() != null ? Constants.Event.UPDATE : Constants.Event.CREATE,
                Constants.Module.JENIS_JAMINAN,
                jenisJaminanDTO.getId() != null ? new JSONObject(oldJenisJaminan).toString() : null,
                new JSONObject(jenisJaminan).toString(),
                jenisJaminanDTO.getId() != null ? Constants.Remark.UPDATE_JENIS_JAMINAN_WAITING_APPROVAL : Constants.Remark.CREATE_JENIS_JAMINAN_WAITING_APPROVAL,
                userLogin );
        return Boolean.TRUE;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<JenisJaminanViewDTO> findJenisJaminanById(Long id) {
        return jenisJaminanRepository.findById(id).map(jenisJaminan -> JenisJaminanMapper.INSTANCE.toDto(jenisJaminan, new JenisJaminanViewDTO()));
    }

    @Override
    @Transactional
    public Boolean approvalJenisJaminan(GlobalApprovalDTO globalApprovalDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        Optional<JenisJaminan> jenisJaminan = jenisJaminanRepository.findById(globalApprovalDTO.getId());
        if (jenisJaminan.isPresent() && !jenisJaminan.get().getModifiedBy().equalsIgnoreCase(authentication.getName())){
            JenisJaminan oldJenisJaminan  = (JenisJaminan) SerializationUtils.clone(jenisJaminan.get());
            if (globalApprovalDTO.getApproval() && jenisJaminan.get().getStatus().toUpperCase().equalsIgnoreCase(Constants.MasterDataStatus.WAITING_FOR_APPROVAL)){
                jenisJaminan.get().setStatus(Constants.MasterDataStatus.ACTIVAT);
                jenisJaminan.get().setActivated(Boolean.TRUE);
                jenisJaminan.get().setApprovedBy(authentication.getName());
                jenisJaminan.get().setApprovedDate(Instant.now());

                //TODO AUDIT TRAIL APPROVAL NEW CREATED / UPDATING JENIS JAMINAN --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.JENIS_JAMINAN,
                        new JSONObject(oldJenisJaminan).toString(),
                        new JSONObject(jenisJaminan.get()).toString(),
                        Constants.Remark.CREATE_UPDATE_JENIS_JAMINAN_STATUS_APPROVED,
                        userLogin );
            }else if (globalApprovalDTO.getApproval() && jenisJaminan.get().getStatus().toUpperCase().equalsIgnoreCase(Constants.MasterDataStatus.WAITING_FOR_DELETE_APPROVAL)){
                jenisJaminan.get().setStatus(Constants.MasterDataStatus.INACTIVE);
                jenisJaminan.get().setActivated(Boolean.FALSE);
                jenisJaminan.get().setApprovedBy(authentication.getName());
                jenisJaminan.get().setApprovedDate(Instant.now());

                //TODO AUDIT TRAIL APPROVAL FOR DELETE JENIS JAMINAN --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.JENIS_JAMINAN,
                        new JSONObject(oldJenisJaminan).toString(),
                        new JSONObject(jenisJaminan.get()).toString(),
                        Constants.Remark.DELETE_JENIS_JAMINAN_STATUS_APPROVED,
                        userLogin );
            }else if (!globalApprovalDTO.getApproval() && jenisJaminan.get().getStatus().toUpperCase().equalsIgnoreCase(Constants.MasterDataStatus.WAITING_FOR_DELETE_APPROVAL)){
                jenisJaminan.get().setStatus(Constants.MasterDataStatus.ACTIVAT);
                jenisJaminan.get().setActivated(Boolean.TRUE);
                jenisJaminan.get().setApprovedBy(authentication.getName());
                jenisJaminan.get().setApprovedDate(Instant.now());

                //TODO AUDIT TRAIL REJECT FOR DELETE JENIS JAMINAN --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.JENIS_JAMINAN,
                        new JSONObject(oldJenisJaminan).toString(),
                        new JSONObject(jenisJaminan.get()).toString(),
                        Constants.Remark.DELETE_JENIS_JAMINAN_STATUS_REJECTED,
                        userLogin );
            }else {
                if (globalApprovalDTO.getNotes().isEmpty()){
                    throw new BadRequestAlertException("Komentar tidak boleh kosong", "", "");
                }
                jenisJaminan.get().setStatus(Constants.MasterDataStatus.REJECT);
                jenisJaminan.get().setNotes(globalApprovalDTO.getNotes());

                //TODO AUDIT TRAIL REJECT FOR NEW DATA / UPDATE DATA JENIS JAMINAN --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.JENIS_JAMINAN,
                        new JSONObject(oldJenisJaminan).toString(),
                        new JSONObject(jenisJaminan.get()).toString(),
                        Constants.Remark.CREATE_UPDATE_JENIS_JAMINAN_STATUS_REJECTED,
                        userLogin );
            }
        }else if (jenisJaminan.get().getModifiedBy().equalsIgnoreCase(authentication.getName())){
            throw new AjoException("User Not Authorized!");
        }
        else {
            throw new AjoException("Jenis Jaminan Not Found");
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean deleteJenisJaminan(List<IdDTO> idDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        idDTO.forEach(idDTO1 ->{
            Optional<JenisJaminan> jenisJaminan = jenisJaminanRepository.findById(idDTO1.getId());
            if (jenisJaminan.isPresent()){
                JenisJaminan oldJenisJaminan  = (JenisJaminan) SerializationUtils.clone(jenisJaminan.get());
                jenisJaminan.get().setStatus(Constants.MasterDataStatus.WAITING_FOR_DELETE_APPROVAL);
                jenisJaminanRepository.save(jenisJaminan.get());

                //TODO AUDIT TRAIL DELETE JENIS JAMINAN BUT WAITING APPROVAL --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.JENIS_JAMINAN,
                        new JSONObject(oldJenisJaminan).toString(),
                        new JSONObject(jenisJaminan.get()).toString(),
                        Constants.Remark.DELETE_JENIS_JAMINAN_WAITING_APPROVAL,
                        userLogin );

            }else {
                throw new AjoException("Id "+idDTO1.getId()+" Jenis Jaminan Not found");
            }
        });
        return Boolean.TRUE;
    }


}
