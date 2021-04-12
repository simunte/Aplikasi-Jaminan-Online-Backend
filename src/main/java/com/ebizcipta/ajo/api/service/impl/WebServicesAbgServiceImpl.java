package com.ebizcipta.ajo.api.service.impl;

import com.ebizcipta.ajo.api.domain.Beneficiary;
import com.ebizcipta.ajo.api.domain.WebServicesAbg;
import com.ebizcipta.ajo.api.exception.AjoException;
import com.ebizcipta.ajo.api.exception.BadRequestAlertException;
import com.ebizcipta.ajo.api.repositories.BeneficiaryRepository;
import com.ebizcipta.ajo.api.repositories.WebServicesAbgRepository;
import com.ebizcipta.ajo.api.service.WebServicesAbgService;
import com.ebizcipta.ajo.api.service.dto.GlobalApprovalDTO;
import com.ebizcipta.ajo.api.service.dto.IdDTO;
import com.ebizcipta.ajo.api.service.dto.WebServicesAbgDTO;
import com.ebizcipta.ajo.api.service.dto.WebServicesAbgViewDTO;
import com.ebizcipta.ajo.api.service.mapper.WebServicesAbgMapper;
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
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class WebServicesAbgServiceImpl implements WebServicesAbgService {
    @Autowired
    private WebServicesAbgRepository webServicesAbgRepository;
    @Autowired
    private BeneficiaryRepository beneficiaryRepository;
    @Autowired
    private AuditTrailUtil auditTrailUtil;

    @Override
    @Transactional(readOnly = true)
    public List<WebServicesAbgViewDTO> findAllWebServicesAbg(String status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (status == null || status == "" || status.equalsIgnoreCase("null")){
            return webServicesAbgRepository.findByStatusNot(Constants.MasterDataStatus.INACTIVE).stream()
                    .map(webServicesAbg -> WebServicesAbgMapper.INSTANCE.toDTO(webServicesAbg, new WebServicesAbgViewDTO()))
                    .collect(Collectors.toCollection(LinkedList::new));
        }else {
            return webServicesAbgRepository.findByStatusAndModifiedByNot(status, authentication.getName()).stream()
                    .map(webServicesAbg -> WebServicesAbgMapper.INSTANCE.toDTO(webServicesAbg, new WebServicesAbgViewDTO()))
                    .collect(Collectors.toCollection(LinkedList::new));
        }
    }

    @Override
    @Transactional
    public Boolean saveWebServicesAbg(WebServicesAbgDTO dto) {
        WebServicesAbg entity = new WebServicesAbg();
        Beneficiary beneficiary = beneficiaryRepository.findById(dto.getBeneficiaryId())
                .orElseThrow(()->new EntityNotFoundException("Beneficiary Not Found"));
        entity.setBeneficiary(beneficiary);
        WebServicesAbg oldWebServicesAbg = null;
        if (dto.getId() != null){
            WebServicesAbg entityFound = webServicesAbgRepository.findById(dto.getId())
                    .orElseThrow(()-> new EntityNotFoundException("Web Service ABG Not Found"));
            oldWebServicesAbg = (WebServicesAbg) SerializationUtils.clone(entityFound);
            entity = WebServicesAbgMapper.INSTANCE.toEntity(dto, entityFound);
        }else {
            entity = WebServicesAbgMapper.INSTANCE.toEntity(dto, entity);
        }
        entity.setStatus(Constants.MasterDataStatus.WAITING_FOR_APPROVAL);
        entity.setActivated(Boolean.FALSE);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        webServicesAbgRepository.save(entity);

        //TODO AUDIT TRAIL SAVE ABG WEB SERVICES --INSERT --DONE
        auditTrailUtil.saveAudit(
                dto.getId() != null ? Constants.Event.UPDATE : Constants.Event.CREATE,
                Constants.Module.ABG_WEBSERVICES,
                dto.getId() != null ? new JSONObject(oldWebServicesAbg).toString() : null,
                new JSONObject(entity).toString(),
                dto.getId() != null ?  Constants.Remark.UPDATE_ABGWEBSERVICE_WAITING_APPROVAL : Constants.Remark.CREATE_ABGWEBSERVICE_WAITING_APPROVAL,
                userLogin );
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Optional<WebServicesAbgViewDTO> findWebServicesAbgById(Long id) {
        return webServicesAbgRepository.findById(id).map(webServicesAbg -> WebServicesAbgMapper.INSTANCE.toDTO(webServicesAbg, new WebServicesAbgViewDTO()));
    }

    @Override
    @Transactional
    public Boolean approvalWebServicesAbg(GlobalApprovalDTO globalApprovalDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        Optional<WebServicesAbg> webServicesAbg = webServicesAbgRepository.findById(globalApprovalDTO.getId());

        if (webServicesAbg.isPresent() && !webServicesAbg.get().getModifiedBy().equalsIgnoreCase(authentication.getName())){
            WebServicesAbg oldWebServicesAbg = (WebServicesAbg) SerializationUtils.clone(webServicesAbg.get());
            if (globalApprovalDTO.getApproval() && webServicesAbg.get().getStatus().toUpperCase().equalsIgnoreCase(Constants.MasterDataStatus.WAITING_FOR_APPROVAL)){
                webServicesAbg.get().setStatus(Constants.MasterDataStatus.ACTIVAT);
                webServicesAbg.get().setActivated(Boolean.TRUE);
                webServicesAbg.get().setApprovedBy(authentication.getName());
                webServicesAbg.get().setApprovedDate(Instant.now());

                //TODO AUDIT TRAIL UPDATE ABG WEB SERVICES --APPROVAL NEW- STATUS ACTIVE --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.ABG_WEBSERVICES,
                        new JSONObject(oldWebServicesAbg).toString(),
                        new JSONObject(webServicesAbg.get()).toString(),
                        Constants.Remark.CREATE_UPDATE_ABGWEBSERVICE_STATUS_APPROVED,
                        userLogin );
            }else if (globalApprovalDTO.getApproval() && webServicesAbg.get().getStatus().toUpperCase().equalsIgnoreCase(Constants.MasterDataStatus.WAITING_FOR_DELETE_APPROVAL)){
                webServicesAbg.get().setStatus(Constants.MasterDataStatus.INACTIVE);
                webServicesAbg.get().setActivated(Boolean.FALSE);
                webServicesAbg.get().setApprovedBy(authentication.getName());
                webServicesAbg.get().setApprovedDate(Instant.now());

                //TODO AUDIT TRAIL DELETE ABG WEB SERVICES --STATUS APPROVED --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.ABG_WEBSERVICES,
                        new JSONObject(oldWebServicesAbg).toString(),
                        new JSONObject(webServicesAbg.get()).toString(),
                        Constants.Remark.DELETE_ABGWEBSERVICE_STATUS_APPROVED,
                        userLogin );
            }else if (!globalApprovalDTO.getApproval() && webServicesAbg.get().getStatus().toUpperCase().equalsIgnoreCase(Constants.MasterDataStatus.WAITING_FOR_DELETE_APPROVAL)){
                webServicesAbg.get().setStatus(Constants.MasterDataStatus.ACTIVAT);
                webServicesAbg.get().setActivated(Boolean.TRUE);
                webServicesAbg.get().setApprovedBy(authentication.getName());
                webServicesAbg.get().setApprovedDate(Instant.now());

                //TODO AUDIT TRAIL DELETE ABG WEB SERVICES --STATUS REJECTED --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.ABG_WEBSERVICES,
                        new JSONObject(oldWebServicesAbg).toString(),
                        new JSONObject(webServicesAbg.get()).toString(),
                        Constants.Remark.DELETE_ABGWEBSERVICE_STATUS_REJECTED,
                        userLogin );
            }else {
                if (globalApprovalDTO.getNotes().isEmpty()){
                    throw new BadRequestAlertException("Notes can't be Empty", "", "");
                }
                webServicesAbg.get().setStatus(Constants.MasterDataStatus.REJECT);
                webServicesAbg.get().setNotes(globalApprovalDTO.getNotes());

                //TODO AUDIT TRAIL UPDATE ABG WEB SERVICES -- STATUS REJECTED --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.ABG_WEBSERVICES,
                        new JSONObject(oldWebServicesAbg).toString(),
                        new JSONObject(webServicesAbg.get()).toString(),
                        Constants.Remark.CREATE_UPDATE_ABGWEBSERVICE_STATUS_REJECTED,
                        userLogin );
            }
            webServicesAbgRepository.save(webServicesAbg.get());
        }else if (webServicesAbg.get().getModifiedBy().equalsIgnoreCase(authentication.getName())){
            throw new AjoException("User Not Authorized!");
        }else {
            throw new AjoException("Web Service Abg Not Found");
        }

        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean deleteWebServicesAbg(List<IdDTO> idDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        idDTO.forEach(idDTO1 ->{
            Optional<WebServicesAbg> webServicesAbg = webServicesAbgRepository.findById(idDTO1.getId());
            if (webServicesAbg.isPresent()){
                WebServicesAbg oldWebServicesAbg =  (WebServicesAbg) SerializationUtils.clone(webServicesAbg.get());
                webServicesAbg.get().setStatus(Constants.MasterDataStatus.WAITING_FOR_DELETE_APPROVAL);
                webServicesAbgRepository.save(webServicesAbg.get());

                //TODO AUDIT TRAIL UPDATE ABG WEB SERVICES --DELETE BUT WAITING FOR APPROVAL --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.ABG_WEBSERVICES,
                        new JSONObject(oldWebServicesAbg).toString(),
                        new JSONObject(webServicesAbg.get()).toString(),
                        Constants.Remark.DELETE_ABGWEBSERVICE_WAITING_APPROVAL,
                        userLogin );
            }else {
                throw new AjoException("Id "+idDTO1.getId()+" Web Services Abg Not found");
            }
        });
        return Boolean.TRUE;
    }
}
