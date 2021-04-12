package com.ebizcipta.ajo.api.service.impl;

import com.ebizcipta.ajo.api.domain.UnitPengguna;
import com.ebizcipta.ajo.api.exception.AjoException;
import com.ebizcipta.ajo.api.exception.BadRequestAlertException;
import com.ebizcipta.ajo.api.repositories.UnitPenggunaRepository;
import com.ebizcipta.ajo.api.service.UnitPenggunaService;
import com.ebizcipta.ajo.api.service.dto.GlobalApprovalDTO;
import com.ebizcipta.ajo.api.service.dto.IdDTO;
import com.ebizcipta.ajo.api.service.dto.UnitPenggunaDTO;
import com.ebizcipta.ajo.api.service.dto.UnitPenggunaVIewDTO;
import com.ebizcipta.ajo.api.service.mapper.UnitPenggunaMapper;
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
public class UnitPenggunaServiceImpl implements UnitPenggunaService {
    @Autowired
    private UnitPenggunaRepository unitPenggunaRepository;

    @Autowired
    private AuditTrailUtil auditTrailUtil;

    @Override
    @Transactional(readOnly = true)
    public List<UnitPenggunaVIewDTO> findAllUnitPengguna(String status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (status == null || status == "" || status.equalsIgnoreCase("null")){
            return unitPenggunaRepository.findByStatusNot(Constants.MasterDataStatus.INACTIVE).stream()
                    .map(unitPengguna -> UnitPenggunaMapper.INSTANCE.toDto(unitPengguna, new UnitPenggunaVIewDTO()))
                    .collect(Collectors.toCollection(LinkedList::new));
        }else {
            return unitPenggunaRepository.findByStatusAndModifiedByNot(status, authentication.getName()).stream()
                    .map(unitPengguna -> UnitPenggunaMapper.INSTANCE.toDto(unitPengguna, new UnitPenggunaVIewDTO()))
                    .collect(Collectors.toCollection(LinkedList::new));
        }
    }

    @Override
    @Transactional
    public Boolean saveUnitPengguna(UnitPenggunaDTO unitPenggunaDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        UnitPengguna unitPengguna = new UnitPengguna();
        UnitPengguna oldUnitPengguna = null;
        if (unitPenggunaDTO.getId()!= null){
            UnitPengguna unitPenggunaFound = unitPenggunaRepository.findById(unitPenggunaDTO.getId())
                    .orElseThrow(()->new EntityNotFoundException("Unit Pengguna Not Found"));
            oldUnitPengguna = (UnitPengguna) SerializationUtils.clone(unitPenggunaFound);
            unitPengguna = UnitPenggunaMapper.INSTANCE.toEntity(unitPenggunaDTO, unitPenggunaFound);
        }else {
            List<UnitPengguna> checkUnitPengguna = unitPenggunaRepository.findByCodeUnitPenggunaOrUnitPenggunaEqualsIgnoreCase(unitPenggunaDTO.getCodeUnitPengguna() , unitPenggunaDTO.getUnitPengguna())
                    .stream()
                    .filter(x -> !Statusutil.getStatusForMasterDataUpdate().contains(x.getStatus()))
                    .collect(Collectors.toCollection(LinkedList::new));
            if(!checkUnitPengguna.isEmpty()){
                throw new AjoException("Kode / Unit Pengguna tidak boleh sama");
            }
            unitPengguna = UnitPenggunaMapper.INSTANCE.toEntity(unitPenggunaDTO, unitPengguna);
        }
        unitPengguna.setStatus(Constants.MasterDataStatus.WAITING_FOR_APPROVAL);
        unitPengguna.setActivated(Boolean.FALSE);
        unitPenggunaRepository.save(unitPengguna);

        //TODO AUDIT TRAIL CREATE NEW UNIT PENGGUNA --DONE
        auditTrailUtil.saveAudit(
                unitPenggunaDTO.getId()!= null ? Constants.Event.UPDATE : Constants.Event.CREATE,
                Constants.Module.UNIT_PENGGUNA,
                unitPenggunaDTO.getId()!= null ? new JSONObject(oldUnitPengguna).toString() : null,
                new JSONObject(unitPengguna).toString(),
                unitPenggunaDTO.getId()!= null ? Constants.Remark.UPDATE_UNITPENGGUNA_WAITING_APPROVAL : Constants.Remark.CREATE_UNITPENGGUNA_WAITING_APPROVAL,
                userLogin );

        return Boolean.TRUE;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UnitPenggunaVIewDTO> findJUnitPenggunaById(Long id) {
        return unitPenggunaRepository.findById(id).map(unitPengguna -> UnitPenggunaMapper.INSTANCE.toDto(unitPengguna, new UnitPenggunaVIewDTO()));
    }

    @Override
    @Transactional
    public Boolean approvalUnitPengguna(GlobalApprovalDTO globalApprovalDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        Optional<UnitPengguna> unitPengguna = unitPenggunaRepository.findById(globalApprovalDTO.getId());
        if (unitPengguna.isPresent() && !unitPengguna.get().getModifiedBy().equalsIgnoreCase(authentication.getName())){
            UnitPengguna oldUnitPengguna = (UnitPengguna) SerializationUtils.clone(unitPengguna.get());
            if (globalApprovalDTO.getApproval() && unitPengguna.get().getStatus().toUpperCase().equalsIgnoreCase(Constants.MasterDataStatus.WAITING_FOR_APPROVAL)){
                unitPengguna.get().setStatus(Constants.MasterDataStatus.ACTIVAT);
                unitPengguna.get().setActivated(Boolean.TRUE);
                unitPengguna.get().setApprovedBy(authentication.getName());
                unitPengguna.get().setApprovedDate(Instant.now());

                //TODO AUDIT TRAIL APPROVAL NEW CREATED UNIT PENGGUNA --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.UNIT_PENGGUNA,
                        new JSONObject(oldUnitPengguna).toString(),
                        new JSONObject(unitPengguna).toString(),
                        Constants.Remark.CREATE_UPDATE_UNITPENGGUNA_STATUS_APPROVED,
                        userLogin );
            }else if (globalApprovalDTO.getApproval() && unitPengguna.get().getStatus().toUpperCase().equalsIgnoreCase(Constants.MasterDataStatus.WAITING_FOR_DELETE_APPROVAL)){
                unitPengguna.get().setStatus(Constants.MasterDataStatus.INACTIVE);
                unitPengguna.get().setActivated(Boolean.FALSE);
                unitPengguna.get().setApprovedBy(authentication.getName());
                unitPengguna.get().setApprovedDate(Instant.now());

                //TODO AUDIT TRAIL DELETE UNIT PENGGUNA STATUS APPROVED --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.UNIT_PENGGUNA,
                        new JSONObject(oldUnitPengguna).toString(),
                        new JSONObject(unitPengguna).toString(),
                        Constants.Remark.DELETE_UNITPENGGUNA_STATUS_APPROVED,
                        userLogin );
            }else if (!globalApprovalDTO.getApproval() && unitPengguna.get().getStatus().toUpperCase().equalsIgnoreCase(Constants.MasterDataStatus.WAITING_FOR_DELETE_APPROVAL)){
                unitPengguna.get().setStatus(Constants.MasterDataStatus.ACTIVAT);
                unitPengguna.get().setActivated(Boolean.TRUE);
                unitPengguna.get().setApprovedBy(authentication.getName());
                unitPengguna.get().setApprovedDate(Instant.now());

                //TODO AUDIT TRAIL DELETE UNIT PENGGUNA STATUS REJECTED --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.UNIT_PENGGUNA,
                        new JSONObject(oldUnitPengguna).toString(),
                        new JSONObject(unitPengguna).toString(),
                        Constants.Remark.DELETE_UNITPENGGUNA_STATUS_REJECTED,
                        userLogin );
            }else {
                if (globalApprovalDTO.getNotes().isEmpty()){
                    throw new BadRequestAlertException("Komentar tidak boleh kosong", "", "");
                }
                unitPengguna.get().setStatus(Constants.MasterDataStatus.REJECT);
                unitPengguna.get().setNotes(globalApprovalDTO.getNotes());

                //TODO AUDIT TRAIL UPDATE UNIT PENGGUNA STATUS REJECTED --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.UNIT_PENGGUNA,
                        new JSONObject(oldUnitPengguna).toString(),
                        new JSONObject(unitPengguna).toString(),
                        Constants.Remark.CREATE_UPDATE_UNITPENGGUNA_STATUS_REJECTED,
                        userLogin );
            }
            unitPenggunaRepository.save(unitPengguna.get());
        }else if (unitPengguna.get().getModifiedBy().equalsIgnoreCase(authentication.getName())){
            throw new AjoException("User Not Authorized!");
        }else {
            throw new AjoException("Unit Pengguna Not Found");
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean deleteUnitPengguna(List<IdDTO> idDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        idDTO.forEach(idDTO1 ->{
            Optional<UnitPengguna> unitPengguna = unitPenggunaRepository.findById(idDTO1.getId());
            if (unitPengguna.isPresent()){
                UnitPengguna oldUnitPengguna = (UnitPengguna) SerializationUtils.clone(unitPengguna.get());

                unitPengguna.get().setStatus(Constants.MasterDataStatus.WAITING_FOR_DELETE_APPROVAL);
                unitPenggunaRepository.save(unitPengguna.get());
                
                //TODO AUDIT TRAIL DELETE FOR UNIT PENGGUNA WAITING APPROVAL --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.UNIT_PENGGUNA,
                        new JSONObject(oldUnitPengguna).toString(),
                        new JSONObject(unitPengguna.get()).toString(),
                        Constants.Remark.DELETE_UNITPENGGUNA_WAITING_APPROVAL,
                        userLogin );

            }else {
                throw new AjoException("Id "+idDTO1.getId()+" Unit Pengguna Not found");
            }
        });
        return Boolean.TRUE;
    }


}
