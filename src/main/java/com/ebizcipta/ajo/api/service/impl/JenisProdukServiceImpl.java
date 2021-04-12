package com.ebizcipta.ajo.api.service.impl;

import com.ebizcipta.ajo.api.domain.JenisJaminan;
import com.ebizcipta.ajo.api.domain.JenisProduk;
import com.ebizcipta.ajo.api.exception.AjoException;
import com.ebizcipta.ajo.api.exception.BadRequestAlertException;
import com.ebizcipta.ajo.api.repositories.JenisProdukRepository;
import com.ebizcipta.ajo.api.service.JenisProdukService;
import com.ebizcipta.ajo.api.service.dto.GlobalApprovalDTO;
import com.ebizcipta.ajo.api.service.dto.IdDTO;
import com.ebizcipta.ajo.api.service.dto.JenisProdukDTO;
import com.ebizcipta.ajo.api.service.dto.JenisProdukViewDTO;
import com.ebizcipta.ajo.api.service.mapper.JenisProdukMapper;
import com.ebizcipta.ajo.api.util.AuditTrailUtil;
import com.ebizcipta.ajo.api.util.Constants;
import com.ebizcipta.ajo.api.util.Statusutil;
import com.ebizcipta.ajo.api.util.WebServicesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class JenisProdukServiceImpl implements JenisProdukService {
    @Autowired
    JenisProdukRepository jenisProdukRepository;
    @Autowired
    WebServicesUtil webServicesUtil;
    @Autowired
    private AuditTrailUtil auditTrailUtil;

    @Override
    @Transactional(readOnly = true)
    public List<JenisProdukViewDTO> findAllJenisProduk(String status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (status == null || status == "" || status.equalsIgnoreCase("null")){
            return jenisProdukRepository.findByStatusNot(Constants.MasterDataStatus.INACTIVE).stream()
                    .map(jenisProduk -> JenisProdukMapper.INSTANCE.toDto(jenisProduk, new JenisProdukViewDTO()))
                    .collect(Collectors.toCollection(LinkedList::new));
        }else {
            return jenisProdukRepository.findByStatusAndModifiedByNot(status, authentication.getName()).stream()
                    .map(jenisProduk -> JenisProdukMapper.INSTANCE.toDto(jenisProduk, new JenisProdukViewDTO()))
                    .collect(Collectors.toCollection(LinkedList::new));
        }
    }

    @Override
    @Transactional
    public Boolean saveJenisProduk(JenisProdukDTO jenisProdukDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        JenisProduk jenisProduk = new JenisProduk();
        JenisProduk oldJenisProduk = null;
        if (jenisProdukDTO.getId()!=null){
            JenisProduk jenisProdukFound = jenisProdukRepository.findById(jenisProdukDTO.getId())
                    .orElseThrow(()-> new EntityNotFoundException("Jenis Produk Not Found"));
            oldJenisProduk = (JenisProduk) SerializationUtils.clone(jenisProdukFound);
            jenisProduk = JenisProdukMapper.INSTANCE.toEntity(jenisProdukDTO, jenisProdukFound);
        }else {
            List<JenisProduk> checkJenisProduk = jenisProdukRepository.findByCodeProdukOrJenisProdukEqualsIgnoreCase(jenisProdukDTO.getCodeProduk() ,
                    jenisProdukDTO.getJenisProduk())
                    .stream()
                    .filter(jenisProduk1 -> !Statusutil.getStatusForMasterDataUpdate().contains(jenisProduk1.getStatus()))
                    .collect(Collectors.toCollection(LinkedList::new));

            if(!checkJenisProduk.isEmpty()){
                throw new AjoException("Kode / Jenis Produk tidak boleh sama");
            }
            jenisProduk = JenisProdukMapper.INSTANCE.toEntity(jenisProdukDTO, jenisProduk);
        }
        jenisProduk.setStatus(Constants.MasterDataStatus.WAITING_FOR_APPROVAL);
        jenisProduk.setActivated(Boolean.FALSE);
        jenisProdukRepository.save(jenisProduk);

        //TODO AUDIT TRAIL CREATE / UPDATE JENIS PRODUK BUT WAITING APPROVAL --DONE
        auditTrailUtil.saveAudit(
                jenisProdukDTO.getId() != null ? Constants.Event.UPDATE : Constants.Event.CREATE,
                Constants.Module.JENIS_PRODUK,
                jenisProdukDTO.getId() != null ? new JSONObject(oldJenisProduk).toString() : null,
                new JSONObject(jenisProduk).toString(),
                jenisProdukDTO.getId() != null ? Constants.Remark.UPDATE_JENIS_PRODUK_WAITING_APPROVAL : Constants.Remark.CREATE_JENIS_PRODUK_WAITING_APPROVAL,
                userLogin );
        return Boolean.TRUE;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<JenisProdukViewDTO> findJenisProdukById(Long id) {
        return jenisProdukRepository.findById(id).map(jenisProduk -> JenisProdukMapper.INSTANCE.toDto(jenisProduk, new JenisProdukViewDTO()));
    }

    @Override
    @Transactional
    public Boolean approvalJenisProduk(GlobalApprovalDTO globalApprovalDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        Optional<JenisProduk> jenisProduk = jenisProdukRepository.findById(globalApprovalDTO.getId());
        if (jenisProduk.isPresent() && !jenisProduk.get().getModifiedBy().equalsIgnoreCase(authentication.getName())){
            JenisProduk oldJenisProduk = (JenisProduk) SerializationUtils.clone(jenisProduk.get());
            if (globalApprovalDTO.getApproval() && jenisProduk.get().getStatus().toUpperCase().equalsIgnoreCase(Constants.MasterDataStatus.WAITING_FOR_APPROVAL)){
                jenisProduk.get().setStatus(Constants.MasterDataStatus.ACTIVAT);
                jenisProduk.get().setActivated(Boolean.TRUE);
                jenisProduk.get().setApprovedBy(authentication.getName());
                jenisProduk.get().setApprovedDate(Instant.now());

                //TODO AUDIT TRAIL APPROVAL NEW CREATED / UPDATING JENIS PRODUK --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.JENIS_PRODUK,
                        new JSONObject(oldJenisProduk).toString(),
                        new JSONObject(jenisProduk.get()).toString(),
                        Constants.Remark.CREATE_UPDATE_JENIS_PRODUK_STATUS_APPROVED,
                        userLogin );
            }else if (globalApprovalDTO.getApproval() && jenisProduk.get().getStatus().toUpperCase().equalsIgnoreCase(Constants.MasterDataStatus.WAITING_FOR_DELETE_APPROVAL)){
                jenisProduk.get().setStatus(Constants.MasterDataStatus.INACTIVE);
                jenisProduk.get().setActivated(Boolean.FALSE);
                jenisProduk.get().setApprovedBy(authentication.getName());
                jenisProduk.get().setApprovedDate(Instant.now());

                //TODO AUDIT TRAIL APPROVAL FOR DELETE JENIS PRODUK --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.JENIS_PRODUK,
                        new JSONObject(oldJenisProduk).toString(),
                        new JSONObject(jenisProduk.get()).toString(),
                        Constants.Remark.DELETE_JENIS_PRODUK_STATUS_APPROVED,
                        userLogin );
            }else if (!globalApprovalDTO.getApproval() && jenisProduk.get().getStatus().toUpperCase().equalsIgnoreCase(Constants.MasterDataStatus.WAITING_FOR_DELETE_APPROVAL)){
                jenisProduk.get().setStatus(Constants.MasterDataStatus.ACTIVAT);
                jenisProduk.get().setActivated(Boolean.TRUE);
                jenisProduk.get().setApprovedBy(authentication.getName());
                jenisProduk.get().setApprovedDate(Instant.now());

                //TODO AUDIT TRAIL REJECT FOR DELETE JENIS PRODUK --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.JENIS_PRODUK,
                        new JSONObject(oldJenisProduk).toString(),
                        new JSONObject(jenisProduk.get()).toString(),
                        Constants.Remark.DELETE_JENIS_PRODUK_STATUS_REJECTED,
                        userLogin );
            }else {
                if (globalApprovalDTO.getNotes().isEmpty()){
                    throw new BadRequestAlertException("Komentar tidak boleh kosong", "", "");
                }
                jenisProduk.get().setStatus(Constants.MasterDataStatus.REJECT);
                jenisProduk.get().setNotes(globalApprovalDTO.getNotes());

                //TODO AUDIT TRAIL REJECT FOR NEW DATA / UPDATE DATA JENIS PRODUK --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.JENIS_PRODUK,
                        new JSONObject(oldJenisProduk).toString(),
                        new JSONObject(jenisProduk.get()).toString(),
                        Constants.Remark.CREATE_UPDATE_JENIS_PRODUK_STATUS_REJECTED,
                        userLogin );
            }
        }else if (jenisProduk.get().getModifiedBy().equalsIgnoreCase(authentication.getName())){
            throw new AjoException("User Not Authorized!");
        }
        else{
            throw new AjoException("Jenis Produk Not Found");
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean deleteJenisProduk(List<IdDTO> idDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        idDTO.forEach(idDTO1 ->{
            Optional<JenisProduk> jenisProduk = jenisProdukRepository.findById(idDTO1.getId());
            if (jenisProduk.isPresent()){
                JenisProduk oldJenisProduk = (JenisProduk) SerializationUtils.clone(jenisProduk.get());
                jenisProduk.get().setStatus(Constants.MasterDataStatus.WAITING_FOR_DELETE_APPROVAL);
                jenisProdukRepository.save(jenisProduk.get());

                //TODO AUDIT TRAIL DELETE JENIS JAMINAN BUT WAITING APPROVAL --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.JENIS_PRODUK,
                        new JSONObject(oldJenisProduk).toString(),
                        new JSONObject(jenisProduk.get()).toString(),
                        Constants.Remark.DELETE_JENIS_PRODUK_WAITING_APPROVAL,
                        userLogin );
            }else {
                throw new AjoException("Id "+idDTO1.getId()+" Jenis Produk Not found");
            }
        });
        return Boolean.TRUE;
    }


}
