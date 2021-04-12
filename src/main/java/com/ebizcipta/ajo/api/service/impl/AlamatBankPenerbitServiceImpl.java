package com.ebizcipta.ajo.api.service.impl;

import ch.qos.logback.core.status.StatusUtil;
import com.ebizcipta.ajo.api.domain.AlamatBankPenerbit;
import com.ebizcipta.ajo.api.exception.AjoException;
import com.ebizcipta.ajo.api.exception.BadRequestAlertException;
import com.ebizcipta.ajo.api.repositories.AlamatBankPenerbitRepository;
import com.ebizcipta.ajo.api.service.AlamatBankPenerbitService;
import com.ebizcipta.ajo.api.service.dto.AlamatBankPenerbitDTO;
import com.ebizcipta.ajo.api.service.dto.AlamatBankPenerbitViewDTO;
import com.ebizcipta.ajo.api.service.dto.GlobalApprovalDTO;
import com.ebizcipta.ajo.api.service.dto.IdDTO;
import com.ebizcipta.ajo.api.service.mapper.AlamatBankPenerbitMapper;
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
public class AlamatBankPenerbitServiceImpl implements AlamatBankPenerbitService{
    @Autowired
    private AlamatBankPenerbitRepository alamatBankPenerbitRepository;

    @Autowired
    private AuditTrailUtil auditTrailUtil;

    @Override
    public List<AlamatBankPenerbitViewDTO> findAllAlamatBankPenerbit(String status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (status == null || status == "" || status.equalsIgnoreCase("null")){
            return alamatBankPenerbitRepository.findByStatusNot(Constants.MasterDataStatus.INACTIVE).stream()
                    .map(alamatBankPenerbit -> AlamatBankPenerbitMapper.INSTANCE.toDto(alamatBankPenerbit, new AlamatBankPenerbitViewDTO()))
                    .collect(Collectors.toCollection(LinkedList::new));
        }else {
            return alamatBankPenerbitRepository.findByStatusAndModifiedByNot(status, authentication.getName()).stream()
                    .map(alamatBankPenerbit -> AlamatBankPenerbitMapper.INSTANCE.toDto(alamatBankPenerbit, new AlamatBankPenerbitViewDTO()))
                    .collect(Collectors.toCollection(LinkedList::new));
        }
    }

    @Override
    public Boolean saveAlamatBankPenerbit(AlamatBankPenerbitDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        AlamatBankPenerbit entity = new AlamatBankPenerbit();
        AlamatBankPenerbit oldBankPenerbit = null;
        if (dto.getId() != null){
            AlamatBankPenerbit entityFound = alamatBankPenerbitRepository.findById(dto.getId())
                    .orElseThrow(()-> new EntityNotFoundException("Alamat Bank Penerbit Not Found"));
            oldBankPenerbit = (AlamatBankPenerbit) SerializationUtils.clone(entityFound);
            entity = AlamatBankPenerbitMapper.INSTANCE.toEntity(dto, entityFound);
        }else {
            List<AlamatBankPenerbit> checkAlamatBankPenerbit = alamatBankPenerbitRepository.
                    findByCodeOrCabangEqualsIgnoreCaseAndStatusNotIn(dto.getCode() , dto.getAlamatBankPenerbit(), Statusutil.getStatusForMasterDataUpdate());

            if(!checkAlamatBankPenerbit.isEmpty()){
                throw new AjoException("Kode / Alamat bank penerbit tidak boleh sama");
            }
            entity = AlamatBankPenerbitMapper.INSTANCE.toEntity(dto, entity);
        }
        entity.setStatus(Constants.MasterDataStatus.WAITING_FOR_APPROVAL);
        entity.setActivated(Boolean.FALSE);
        alamatBankPenerbitRepository.save(entity);

        //TODO AUDIT TRAIL CREATE ALAMAT BANK PENERBIT - WAITING APPROVAL --DONE
        auditTrailUtil.saveAudit(
                dto.getId() != null ? Constants.Event.UPDATE : Constants.Event.CREATE,
                Constants.Module.BANK_PENERBIT,
                dto.getId() != null ? new JSONObject(oldBankPenerbit).toString() : null,
                new JSONObject(entity).toString(),
                dto.getId() != null ? Constants.Remark.UPDATE_BANKPENERBIT_WAITING_APPROVAL : Constants.Remark.CREATE_BANKPENERBIT_WAITING_APPROVAL,
                userLogin );
        return Boolean.TRUE;
    }

    @Override
    public Optional<AlamatBankPenerbitViewDTO> findAlamatBankPenerbitById(Long id) {
        return alamatBankPenerbitRepository.findById(id).map(alamatBankPenerbit -> AlamatBankPenerbitMapper.INSTANCE.toDto(alamatBankPenerbit, new AlamatBankPenerbitViewDTO()));
    }

    @Override
    public Boolean approvalAlamatBankPenerbit(GlobalApprovalDTO globalApprovalDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        Optional<AlamatBankPenerbit> alamatBankPenerbit = alamatBankPenerbitRepository.findById(globalApprovalDTO.getId());

        if (alamatBankPenerbit.isPresent() && !alamatBankPenerbit.get().getModifiedBy().equalsIgnoreCase(authentication.getName())){
            AlamatBankPenerbit oldAlamatBankPenerbit = (AlamatBankPenerbit) SerializationUtils.clone(alamatBankPenerbit.get());
            if (globalApprovalDTO.getApproval() && alamatBankPenerbit.get().getStatus().toUpperCase().equalsIgnoreCase(Constants.MasterDataStatus.WAITING_FOR_APPROVAL)){
                alamatBankPenerbit.get().setStatus(Constants.MasterDataStatus.ACTIVAT);
                alamatBankPenerbit.get().setActivated(Boolean.TRUE);
                alamatBankPenerbit.get().setApprovedBy(authentication.getName());
                alamatBankPenerbit.get().setApprovedDate(Instant.now());

                //TODO AUDIT TRAIL CREATE ALAMAT BANK PENERBIT - STATUS APPROVED --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.BANK_PENERBIT,
                        new JSONObject(oldAlamatBankPenerbit).toString(),
                        new JSONObject(alamatBankPenerbit.get()).toString(),
                        Constants.Remark.CREATE_UPDATE_BANKPENERBIT_STATUS_APPROVED,
                        userLogin );
            }else if (globalApprovalDTO.getApproval() && alamatBankPenerbit.get().getStatus().toUpperCase().equalsIgnoreCase(Constants.MasterDataStatus.WAITING_FOR_DELETE_APPROVAL)){
                alamatBankPenerbit.get().setStatus(Constants.MasterDataStatus.INACTIVE);
                alamatBankPenerbit.get().setActivated(Boolean.FALSE);
                alamatBankPenerbit.get().setApprovedBy(authentication.getName());
                alamatBankPenerbit.get().setApprovedDate(Instant.now());

                //TODO AUDIT TRAIL DELETE ALAMAT BANK PENERBIT - STATUS APPROVED --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.BANK_PENERBIT,
                        new JSONObject(oldAlamatBankPenerbit).toString(),
                        new JSONObject(alamatBankPenerbit.get()).toString(),
                        Constants.Remark.DELETE_BANKPENERBIT_STATUS_APPROVED,
                        userLogin );
            }else if (!globalApprovalDTO.getApproval() && alamatBankPenerbit.get().getStatus().toUpperCase().equalsIgnoreCase(Constants.MasterDataStatus.WAITING_FOR_DELETE_APPROVAL)){
                alamatBankPenerbit.get().setStatus(Constants.MasterDataStatus.ACTIVAT);
                alamatBankPenerbit.get().setActivated(Boolean.TRUE);
                alamatBankPenerbit.get().setApprovedBy(authentication.getName());
                alamatBankPenerbit.get().setApprovedDate(Instant.now());

                //TODO AUDIT TRAIL DELETE ALAMAT BANK PENERBIT - STATUS REJECTED --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.BANK_PENERBIT,
                        new JSONObject(oldAlamatBankPenerbit).toString(),
                        new JSONObject(alamatBankPenerbit.get()).toString(),
                        Constants.Remark.DELETE_BANKPENERBIT_STATUS_REJECTED,
                        userLogin );
            }else {
                if (globalApprovalDTO.getNotes().isEmpty()){
                    throw new BadRequestAlertException("Notes can't be Empty", "", "");
                }
                alamatBankPenerbit.get().setStatus(Constants.MasterDataStatus.REJECT);
                alamatBankPenerbit.get().setActivated(Boolean.FALSE);

                //TODO AUDIT TRAIL ALAMAT BANK PENERBIT - STATUS REJECTED --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.BANK_PENERBIT,
                        new JSONObject(oldAlamatBankPenerbit).toString(),
                        new JSONObject(alamatBankPenerbit.get()).toString(),
                        Constants.Remark.CREATE_UPDATE_BANKPENERBIT_STATUS_REJECTED,
                        userLogin );
            }
            alamatBankPenerbitRepository.save(alamatBankPenerbit.get());
        }else  if (alamatBankPenerbit.get().getModifiedBy().equalsIgnoreCase(authentication.getName())){
            throw new AjoException("User Not Authorized!");
        }
        else {
            throw new AjoException("Alamat Bank Penerbit Not Found");
        }

        return Boolean.TRUE;
    }

    @Override
    public Boolean deleteAlamatBankPenerbit(List<IdDTO> idDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        idDTO.forEach(data -> {
            Optional<AlamatBankPenerbit> alamatBankPenerbit = alamatBankPenerbitRepository.findById(data.getId());
            if (alamatBankPenerbit.isPresent()){
                AlamatBankPenerbit oldAlamatBankPenerbit = (AlamatBankPenerbit) SerializationUtils.clone(alamatBankPenerbit.get());
                alamatBankPenerbit.get().setStatus(Constants.MasterDataStatus.WAITING_FOR_DELETE_APPROVAL);
                alamatBankPenerbitRepository.save(alamatBankPenerbit.get());

                //TODO AUDIT TRAIL DELETE ALAMAT BANK PENERBIT - WAITING APPROVAL --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.BANK_PENERBIT,
                        new JSONObject(oldAlamatBankPenerbit).toString(),
                        new JSONObject(alamatBankPenerbit.get()).toString(),
                        Constants.Remark.DELETE_BANKPENERBIT_WAITING_APPROVAL,
                        userLogin );
            }else {
                throw new AjoException("Id "+data.getId()+" Alamat Bank Penerbit Not found");
            }
        });
        return Boolean.TRUE;
    }
}
