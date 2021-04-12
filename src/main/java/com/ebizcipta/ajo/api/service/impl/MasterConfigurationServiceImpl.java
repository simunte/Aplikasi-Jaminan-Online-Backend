package com.ebizcipta.ajo.api.service.impl;

import com.ebizcipta.ajo.api.domain.JenisProduk;
import com.ebizcipta.ajo.api.domain.MasterConfiguration;
import com.ebizcipta.ajo.api.exception.AjoException;
import com.ebizcipta.ajo.api.exception.BadRequestAlertException;
import com.ebizcipta.ajo.api.repositories.MasterConfigurationRepository;
import com.ebizcipta.ajo.api.service.MasterConfigurationService;
import com.ebizcipta.ajo.api.service.dto.GlobalApprovalDTO;
import com.ebizcipta.ajo.api.service.dto.IdDTO;
import com.ebizcipta.ajo.api.service.dto.MasterConfigurationDTO;
import com.ebizcipta.ajo.api.service.dto.MasterConfigurationViewDTO;
import com.ebizcipta.ajo.api.service.mapper.MasterConfigurationMapper;
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
public class MasterConfigurationServiceImpl implements MasterConfigurationService {
    @Autowired
    private MasterConfigurationRepository masterConfigurationRepository;
    @Autowired
    private AuditTrailUtil auditTrailUtil;

    @Override
    @Transactional(readOnly = true)
    public List<MasterConfigurationViewDTO> findAllMasterConfiguration(String status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (status == null || status == "" || status.equalsIgnoreCase("null")){
            return masterConfigurationRepository.findByStatusNot(Constants.MasterDataStatus.INACTIVE).stream()
                    .map(masterConfiguration -> MasterConfigurationMapper.INSTANCE.toDto(masterConfiguration, new MasterConfigurationViewDTO()))
                    .collect(Collectors.toCollection(LinkedList::new));
        }else {
            return masterConfigurationRepository.findByStatusAndModifiedByNot(status, authentication.getName()).stream()
                    .map(masterConfiguration -> MasterConfigurationMapper.INSTANCE.toDto(masterConfiguration, new MasterConfigurationViewDTO()))
                    .collect(Collectors.toCollection(LinkedList::new));
        }
    }

    @Override
    @Transactional
    public Boolean saveMasterConfiguration(MasterConfigurationDTO masterConfigurationDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        MasterConfiguration masterConfiguration = new MasterConfiguration();
        MasterConfiguration oldMasterConfiguration = null;
        if (masterConfigurationDTO.getId() != null){
            MasterConfiguration masterConfigurationFound = masterConfigurationRepository.findById(masterConfigurationDTO.getId())
                    .orElseThrow(()-> new EntityNotFoundException("Master Configuration Tidak ditemukan"));
            oldMasterConfiguration = (MasterConfiguration) SerializationUtils.clone(masterConfigurationFound);
            masterConfiguration = MasterConfigurationMapper.INSTANCE.toEntity(masterConfigurationDTO, masterConfigurationFound);
        }else {
            masterConfiguration = MasterConfigurationMapper.INSTANCE.toEntity(masterConfigurationDTO, masterConfiguration);
        }
        masterConfiguration.setStatus(Constants.MasterDataStatus.WAITING_FOR_APPROVAL);
        masterConfiguration.setActivated(Boolean.FALSE);
        masterConfigurationRepository.save(masterConfiguration);

        //TODO AUDIT TRAIL CREATE / UPDATE MASTER CONFIGURATION BUT WAITING APPROVAL
        auditTrailUtil.saveAudit(
                masterConfigurationDTO.getId() != null ? Constants.Event.UPDATE : Constants.Event.CREATE,
                Constants.Module.MASTER_CONFIGURATION,
                masterConfigurationDTO.getId() != null ? new JSONObject(oldMasterConfiguration).toString() : null,
                new JSONObject(masterConfiguration).toString(),
                masterConfigurationDTO.getId() != null ? Constants.Remark.UPDATE_MASTER_CONFIGURATION_WAITING_APPROVAL : Constants.Remark.CREATE_MASTER_CONFIGURATION_WAITING_APPROVAL,
                userLogin );
        return Boolean.TRUE;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MasterConfigurationViewDTO> findMasterConfigurationById(Long id) {
        return masterConfigurationRepository.findTop1ByStatusNotNullOrderByCreationDateDesc()
                .map(masterConfiguration -> MasterConfigurationMapper.INSTANCE.toDto(masterConfiguration, new MasterConfigurationViewDTO()));
    }

    @Override
    @Transactional
    public Boolean approvalMasterConfiguration(GlobalApprovalDTO globalApprovalDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        Optional<MasterConfiguration> masterConfiguration = masterConfigurationRepository.findById(globalApprovalDTO.getId());
        if (masterConfiguration.isPresent() && !masterConfiguration.get().getModifiedBy().equalsIgnoreCase(authentication.getName())){
            MasterConfiguration oldMasterConfiguration = (MasterConfiguration) SerializationUtils.clone(masterConfiguration.get());
            if (globalApprovalDTO.getApproval() && masterConfiguration.get().getStatus().toUpperCase().equalsIgnoreCase(Constants.MasterDataStatus.WAITING_FOR_APPROVAL)){
                masterConfiguration.get().setStatus(Constants.MasterDataStatus.ACTIVAT);
                masterConfiguration.get().setActivated(Boolean.TRUE);
                masterConfiguration.get().setApprovedBy(authentication.getName());
                masterConfiguration.get().setApprovedDate(Instant.now());

                //TODO AUDIT TRAIL APPROVAL NEW CREATED / UPDATING MASTER CONFIG
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.MASTER_CONFIGURATION,
                        new JSONObject(oldMasterConfiguration).toString(),
                        new JSONObject(masterConfiguration.get()).toString(),
                        Constants.Remark.CREATE_UPDATE_MASTER_CONFIGURATION_STATUS_APPROVED,
                        userLogin );
            }else if (globalApprovalDTO.getApproval() && masterConfiguration.get().getStatus().toUpperCase().equalsIgnoreCase(Constants.MasterDataStatus.WAITING_FOR_DELETE_APPROVAL)){
                masterConfiguration.get().setStatus(Constants.MasterDataStatus.INACTIVE);
                masterConfiguration.get().setActivated(Boolean.FALSE);
                masterConfiguration.get().setApprovedBy(authentication.getName());
                masterConfiguration.get().setApprovedDate(Instant.now());

                //TODO AUDIT TRAIL APPROVAL FOR DELETE MASTER CONFIG
                auditTrailUtil.saveAudit(Constants.Event.UPDATE, Constants.Module.MASTER_CONFIGURATION,new JSONObject(oldMasterConfiguration).toString(), new JSONObject(masterConfiguration.get()).toString(), Constants.Remark.DELETE_MASTER_CONFIGURATION_STATUS_APPROVED, userLogin );
            }else if (!globalApprovalDTO.getApproval() && masterConfiguration.get().getStatus().toUpperCase().equalsIgnoreCase(Constants.MasterDataStatus.WAITING_FOR_DELETE_APPROVAL)){
                masterConfiguration.get().setStatus(Constants.MasterDataStatus.ACTIVAT);
                masterConfiguration.get().setActivated(Boolean.TRUE);
                masterConfiguration.get().setApprovedBy(authentication.getName());
                masterConfiguration.get().setApprovedDate(Instant.now());

                //TODO AUDIT TRAIL REJECT FOR DELETE MASTER CONFIG
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.MASTER_CONFIGURATION,
                        new JSONObject(oldMasterConfiguration).toString(),
                        new JSONObject(masterConfiguration.get()).toString(),
                        Constants.Remark.DELETE_MASTER_CONFIGURATION_STATUS_REJECTED,
                        userLogin );
            }else {
                if (globalApprovalDTO.getNotes().isEmpty()){
                    throw new BadRequestAlertException("Notes can't be Empty", "", "");
                }
                masterConfiguration.get().setStatus(Constants.MasterDataStatus.REJECT);
                masterConfiguration.get().setNotes(globalApprovalDTO.getNotes());

                //TODO AUDIT TRAIL REJECT FOR NEW DATA / UPDATE DATA MASTER CONFIG
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.MASTER_CONFIGURATION,
                        new JSONObject(oldMasterConfiguration).toString(),
                        new JSONObject(masterConfiguration.get()).toString(),
                        Constants.Remark.CREATE_UPDATE_MASTER_CONFIGURATION_STATUS_REJECTED,
                        userLogin );
            }
        }else if (masterConfiguration.get().getModifiedBy().equalsIgnoreCase(authentication.getName())){
            throw new AjoException("User Not Authorized!");
        }
        else {
            throw new AjoException("Master Configuration Not Found");
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean deleteMasterConfiguration(List<IdDTO> idDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        idDTO.forEach(idDTO1 ->{
            Optional<MasterConfiguration> masterConfiguration = masterConfigurationRepository.findById(idDTO1.getId());
            if (masterConfiguration.isPresent()){
                MasterConfiguration oldMasterConfiguration = (MasterConfiguration) SerializationUtils.clone(masterConfiguration.get());
                masterConfiguration.get().setStatus(Constants.MasterDataStatus.WAITING_FOR_DELETE_APPROVAL);
                masterConfigurationRepository.save(masterConfiguration.get());

                //TODO AUDIT TRAIL DELETE MASTER CONFIGURATION BUT WAITING APPROVAL
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.MASTER_CONFIGURATION,
                        new JSONObject(oldMasterConfiguration).toString(),
                        new JSONObject(masterConfiguration.get()).toString(),
                        Constants.Remark.DELETE_MASTER_CONFIGURATION_WAITING_APPROVAL,
                        userLogin );
            }else {
                throw new AjoException("Id "+idDTO1.getId()+" Master Configuration Not found");
            }
        });
        return Boolean.TRUE;
    }
}
