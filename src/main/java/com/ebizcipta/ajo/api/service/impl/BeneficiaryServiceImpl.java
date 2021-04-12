package com.ebizcipta.ajo.api.service.impl;

import com.ebizcipta.ajo.api.domain.Beneficiary;
import com.ebizcipta.ajo.api.domain.User;
import com.ebizcipta.ajo.api.exception.AjoException;
import com.ebizcipta.ajo.api.exception.BadRequestAlertException;
import com.ebizcipta.ajo.api.repositories.BeneficiaryRepository;
import com.ebizcipta.ajo.api.repositories.UserRepository;
import com.ebizcipta.ajo.api.service.BeneficiaryService;
import com.ebizcipta.ajo.api.service.dto.BeneficiaryDTO;
import com.ebizcipta.ajo.api.service.dto.BeneficiaryViewDTO;
import com.ebizcipta.ajo.api.service.dto.GlobalApprovalDTO;
import com.ebizcipta.ajo.api.service.dto.IdDTO;
import com.ebizcipta.ajo.api.service.mapper.BeneficiaryMapper;
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
import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class BeneficiaryServiceImpl implements BeneficiaryService{
    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private AuditTrailUtil auditTrailUtil;

    @Override
    @Transactional(readOnly = true)
    public List<BeneficiaryViewDTO> findAllBeneficiary(String status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (status == null || status == "" || status.equalsIgnoreCase("null")){
            return beneficiaryRepository.findByStatusNot(Constants.MasterDataStatus.INACTIVE).stream()
                    .map(beneficiary -> BeneficiaryMapper.INSTANCE.toDto(beneficiary, new BeneficiaryViewDTO()))
                    .collect(Collectors.toCollection(LinkedList::new));
        }else {
            return beneficiaryRepository.findByStatusAndModifiedByNot(status, authentication.getName()).stream()
                    .map(beneficiary -> BeneficiaryMapper.INSTANCE.toDto(beneficiary, new BeneficiaryViewDTO()))
                    .collect(Collectors.toCollection(LinkedList::new));
        }

    }

    @Override
    @Transactional
    public Boolean saveBeneficiary(BeneficiaryDTO beneficiaryDTO) {
        Beneficiary beneficiary = new Beneficiary();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        Beneficiary oldBeneficiary = null;
        if (beneficiaryDTO.getId() != null){
            Beneficiary beneficiaryFound = beneficiaryRepository.findById(beneficiaryDTO.getId())
                    .orElseThrow(()-> new EntityNotFoundException("Beneficiary Not Found"));
            oldBeneficiary = (Beneficiary) SerializationUtils.clone(beneficiaryFound);
            beneficiary = BeneficiaryMapper.INSTANCE.toEntity(beneficiaryDTO, beneficiaryFound);
        }else {
            List<Beneficiary> checkBeneficiary = beneficiaryRepository.findByCodeBeneficiaryOrNamaBeneficiaryEqualsIgnoreCase
                    (beneficiaryDTO.getCodeBeneficiary() , beneficiaryDTO.getNamaBeneficiary())
                    .stream()
                    .filter(x -> !Statusutil.getStatusForMasterDataUpdate().contains(x.getStatus()))
                    .collect(Collectors.toCollection(LinkedList::new));

            if(!checkBeneficiary.isEmpty()){
                throw new AjoException("Kode / beneficiary tidak boleh sama");
            }
            beneficiary = BeneficiaryMapper.INSTANCE.toEntity(beneficiaryDTO, beneficiary);
        }
        beneficiary.setStatus(Constants.MasterDataStatus.WAITING_FOR_APPROVAL);
        beneficiary.setActivated(Boolean.FALSE);
        beneficiaryRepository.save(beneficiary);

        //TODO AUDIT TRAIL SAVE BENEFICIARY DATA BUT WAITING APPROVAL --DONE
        auditTrailUtil.saveAudit(
                beneficiaryDTO.getId() != null ? Constants.Event.UPDATE : Constants.Event.CREATE,
                Constants.Module.BENEFICIARY,
                beneficiaryDTO.getId() != null ? new JSONObject(oldBeneficiary).toString() : null,
                new JSONObject(beneficiary).toString(),
                beneficiaryDTO.getId() != null ? Constants.Remark.UPDATE_BENEFICIARY_WAITING_APPROVAL : Constants.Remark.CREATE_BENEFICIARY_WAITING_APPROVAL,
                userLogin );
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Optional<BeneficiaryViewDTO> findBeneficiaryById(Long id) {
        return beneficiaryRepository.findById(id).map(beneficiary -> BeneficiaryMapper.INSTANCE.toDto(beneficiary, new BeneficiaryViewDTO()));
    }

    @Override
    @Transactional
    public Boolean approvalBeneficiary(GlobalApprovalDTO globalApprovalDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        Optional<Beneficiary> beneficiary = beneficiaryRepository.findById(globalApprovalDTO.getId());
        if (beneficiary.isPresent() && !beneficiary.get().getModifiedBy().equalsIgnoreCase(authentication.getName())){
            Beneficiary oldBeneficiary = (Beneficiary) SerializationUtils.clone(beneficiary.get());
            if (globalApprovalDTO.getApproval() && beneficiary.get().getStatus().toUpperCase().equalsIgnoreCase(Constants.MasterDataStatus.WAITING_FOR_APPROVAL)){
                beneficiary.get().setStatus(Constants.MasterDataStatus.ACTIVAT);
                beneficiary.get().setActivated(Boolean.TRUE);
                beneficiary.get().setApprovedBy(authentication.getName());
                beneficiary.get().setApprovedDate(Instant.now());

                //TODO AUDIT TRAIL APPROVED NEW DATA --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.BENEFICIARY,
                        new JSONObject(oldBeneficiary).toString(),
                        new JSONObject(beneficiary).toString(),
                        Constants.Remark.CREATE_UPDATE_BENEFICIARY_STATUS_APPROVED,
                        userLogin );

            }else if (globalApprovalDTO.getApproval() && beneficiary.get().getStatus().toUpperCase().equalsIgnoreCase(Constants.MasterDataStatus.WAITING_FOR_DELETE_APPROVAL)){
                beneficiary.get().setStatus(Constants.MasterDataStatus.INACTIVE);
                beneficiary.get().setActivated(Boolean.FALSE);
                beneficiary.get().setApprovedBy(authentication.getName());
                beneficiary.get().setApprovedDate(Instant.now());

                //TODO AUDIT TRAIL DELETION DATA STATUS APPROVED --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.BENEFICIARY,
                        new JSONObject(oldBeneficiary).toString(),
                        new JSONObject(beneficiary).toString(),
                        Constants.Remark.DELETE_BENEFICIARY_STATUS_APPROVED,
                        userLogin );

            }else if (!globalApprovalDTO.getApproval() && beneficiary.get().getStatus().toUpperCase().equalsIgnoreCase(Constants.MasterDataStatus.WAITING_FOR_DELETE_APPROVAL)){
                beneficiary.get().setStatus(Constants.MasterDataStatus.ACTIVAT);
                beneficiary.get().setActivated(Boolean.TRUE);
                beneficiary.get().setApprovedBy(authentication.getName());
                beneficiary.get().setApprovedDate(Instant.now());

                //TODO AUDIT TRAIL DELETION DATA STATUS REJECTED --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.BENEFICIARY,
                        new JSONObject(oldBeneficiary).toString(),
                        new JSONObject(beneficiary).toString(),
                        Constants.Remark.DELETE_BENEFICIARY_STATUS_REJECTED,
                        userLogin );

            }else {
                if (globalApprovalDTO.getNotes().isEmpty()){
                    throw new BadRequestAlertException("Komentar tidak boleh kosong", "", "");
                }
                beneficiary.get().setStatus(Constants.MasterDataStatus.REJECT);
                beneficiary.get().setNotes(globalApprovalDTO.getNotes());

                //TODO AUDIT TRAIL UPDATING BENEF STATUS REJECTED --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.BENEFICIARY ,
                        new JSONObject(oldBeneficiary).toString(),
                        new JSONObject(beneficiary).toString(),
                        Constants.Remark.CREATE_UPDATE_BENEFICIARY_STATUS_REJECTED,
                        userLogin );

            }
            beneficiaryRepository.save(beneficiary.get());
        }else if (beneficiary.get().getModifiedBy().equalsIgnoreCase(authentication.getName())){
            throw new AjoException("User Not Authorized!");
        }else {
            throw new AjoException("Beneficiary Not Found");
        }

        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean deleteBeneficiary(List<IdDTO> idDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        idDTO.forEach(idDTO1 ->{
            Optional<Beneficiary> beneficiary = beneficiaryRepository.findById(idDTO1.getId());
            if (beneficiary.isPresent()){
                Beneficiary oldBeneficiary = (Beneficiary) SerializationUtils.clone(beneficiary.get());
                beneficiary.get().setStatus(Constants.MasterDataStatus.WAITING_FOR_DELETE_APPROVAL);
                beneficiaryRepository.save(beneficiary.get());

                //TODO AUDIT TRAIL DELETE BENEFICIARY DATA BUT WAITING APPROVAL --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE ,
                        Constants.Module.BENEFICIARY ,
                        new JSONObject(oldBeneficiary).toString(),
                        new JSONObject(beneficiary.get()).toString(),
                        Constants.Remark.DELETE_BENEFICIARY_WAITING_APPROVAL,
                        userLogin );

            }else {
                throw new AjoException("Id "+idDTO1.getId()+" Beneficiary Not found");
            }
        });
        return Boolean.TRUE;
    }
}
