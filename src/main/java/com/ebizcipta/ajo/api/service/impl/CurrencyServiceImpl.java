package com.ebizcipta.ajo.api.service.impl;

import com.ebizcipta.ajo.api.domain.Currency;
import com.ebizcipta.ajo.api.exception.AjoException;
import com.ebizcipta.ajo.api.exception.BadRequestAlertException;
import com.ebizcipta.ajo.api.repositories.CurrencyRepository;
import com.ebizcipta.ajo.api.service.CurrencyService;
import com.ebizcipta.ajo.api.service.dto.CurrencyDTO;
import com.ebizcipta.ajo.api.service.dto.CurrencyViewDTO;
import com.ebizcipta.ajo.api.service.dto.GlobalApprovalDTO;
import com.ebizcipta.ajo.api.service.dto.IdDTO;
import com.ebizcipta.ajo.api.service.mapper.CurrencyMapper;
import com.ebizcipta.ajo.api.util.AuditTrailUtil;
import com.ebizcipta.ajo.api.util.Constants;
import com.ebizcipta.ajo.api.util.Statusutil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationUtils;
import org.aspectj.apache.bcel.classfile.Constant;
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
public class CurrencyServiceImpl implements CurrencyService{
    @Autowired
    CurrencyRepository currencyRepository;

    @Autowired
    private AuditTrailUtil auditTrailUtil;

    @Override
    @Transactional(readOnly = true)
    public List<CurrencyViewDTO> findAllCurrency(String status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (status == null || status == "" || status.equalsIgnoreCase("null")){
            return currencyRepository.findByStatusNot(Constants.MasterDataStatus.INACTIVE).stream()
                    .map(currency -> CurrencyMapper.INSTANCE.toDto(currency, new CurrencyViewDTO()))
                    .collect(Collectors.toCollection(LinkedList::new));
        }else {
            return currencyRepository.findByStatusAndModifiedByNot(status, authentication.getName()).stream()
                    .map(currency -> CurrencyMapper.INSTANCE.toDto(currency, new CurrencyViewDTO()))
                    .collect(Collectors.toCollection(LinkedList::new));
        }
    }

    @Override
    @Transactional
    public Boolean saveCurrency(CurrencyDTO currencyDTO) {
        Currency currency = new Currency();
        Currency oldCurrency = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        if (currencyDTO.getId()!=null){
            Currency foundCurrency = currencyRepository.findById(currencyDTO.getId())
                    .orElseThrow(()-> new EntityNotFoundException("Currency Not Found"));
            oldCurrency  = (Currency) SerializationUtils.clone(foundCurrency);
            currency = CurrencyMapper.INSTANCE.toEntity(currencyDTO, foundCurrency);
        }else {
            Currency checkCurrency = currencyRepository.findByCurrencyEqualsIgnoreCaseAndStatusNotIn(currencyDTO.getCurrency(), Statusutil.getStatusForMasterDataUpdate());
            if(checkCurrency != null){
                throw new AjoException("Currency tidak boleh sama");
            }
            currency = CurrencyMapper.INSTANCE.toEntity(currencyDTO, currency);
        }
        currency.setStatus(Constants.MasterDataStatus.WAITING_FOR_APPROVAL);
        currency.setActivated(Boolean.FALSE);
        currencyRepository.save(currency);

        //TODO AUDIT TRAIL CREATE / UPDATE CURRENCY BUT WAITING APPROVAL --DONE
        auditTrailUtil.saveAudit(
                currencyDTO.getId()!=null ? Constants.Event.UPDATE : Constants.Event.CREATE,
                Constants.Module.CURRENCY,
                currencyDTO.getId()!=null ? new JSONObject(oldCurrency).toString() : null,
                new JSONObject(currency).toString(),
                currencyDTO.getId()!=null ? Constants.Remark.UPDATE_CURRENCY_WAITING_APPROVAL : Constants.Remark.CREATE_CURRENCY_WAITING_APPROVAL ,
                userLogin );
        return Boolean.TRUE;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CurrencyViewDTO> findCurrencyById(Long id) {
        return currencyRepository.findById(id).map(currency -> CurrencyMapper.INSTANCE.toDto(currency, new CurrencyViewDTO()));
    }

    @Override
    @Transactional
    public Boolean approvalCurrency(GlobalApprovalDTO globalApprovalDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        Optional<Currency> currency = currencyRepository.findById(globalApprovalDTO.getId());
        if (currency.isPresent() && !currency.get().getModifiedBy().equalsIgnoreCase(authentication.getName())){
            Currency oldCurrency  = (Currency) SerializationUtils.clone(currency.get());
            if (globalApprovalDTO.getApproval() && currency.get().getStatus().toUpperCase().equalsIgnoreCase(Constants.MasterDataStatus.WAITING_FOR_APPROVAL)){
                currency.get().setStatus(Constants.MasterDataStatus.ACTIVAT);
                currency.get().setActivated(Boolean.TRUE);
                currency.get().setApprovedBy(authentication.getName());
                currency.get().setApprovedDate(Instant.now());

                //TODO AUDIT TRAIL APPROVAL NEW CREATED / UPDATING CURRENCY --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE ,
                        Constants.Module.CURRENCY,
                        new JSONObject(oldCurrency).toString(),
                        new JSONObject(currency.get()).toString(),
                        Constants.Remark.CREATE_UPDATE_CURRENCY_STATUS_APPROVED ,
                        userLogin );

            }else if (globalApprovalDTO.getApproval() && currency.get().getStatus().toUpperCase().equalsIgnoreCase(Constants.MasterDataStatus.WAITING_FOR_DELETE_APPROVAL)){
                currency.get().setStatus(Constants.MasterDataStatus.INACTIVE);
                currency.get().setActivated(Boolean.FALSE);
                currency.get().setApprovedBy(authentication.getName());
                currency.get().setApprovedDate(Instant.now());

                //TODO AUDIT TRAIL DELETE CURRENCY STATUS APPROVED --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE ,
                        Constants.Module.CURRENCY,
                        new JSONObject(oldCurrency).toString(),
                        new JSONObject(currency.get()).toString(),
                        Constants.Remark.DELETE_CURRENCY_STATUS_APPROVED ,
                        userLogin );
            }else if (!globalApprovalDTO.getApproval() && currency.get().getStatus().toUpperCase().equalsIgnoreCase(Constants.MasterDataStatus.WAITING_FOR_DELETE_APPROVAL)){
                currency.get().setStatus(Constants.MasterDataStatus.ACTIVAT);
                currency.get().setActivated(Boolean.TRUE);
                currency.get().setApprovedBy(authentication.getName());
                currency.get().setApprovedDate(Instant.now());

                //TODO AUDIT TRAIL DELETE CURRENCY STATUS REJECTED --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE ,
                        Constants.Module.CURRENCY,
                        new JSONObject(oldCurrency).toString(),
                        new JSONObject(currency.get()).toString(),
                        Constants.Remark.DELETE_CURRENCY_STATUS_REJECTED ,
                        userLogin );
            }else {
                if (globalApprovalDTO.getNotes().isEmpty()){
                    throw new BadRequestAlertException("Komentar tidak boleh kosong", "", "");
                }
                currency.get().setStatus(Constants.MasterDataStatus.REJECT);
                currency.get().setNotes(globalApprovalDTO.getNotes());

                //TODO AUDIT TRAIL UPDATING CURRENCY STATUS REJECTED --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE ,
                        Constants.Module.CURRENCY,
                        new JSONObject(oldCurrency).toString(),
                        new JSONObject(currency.get()).toString(),
                        Constants.Remark.CREATE_UPDATE_CURRENCY_STATUS_REJECTED ,
                        userLogin );
            }
        }else if (currency.get().getModifiedBy().equalsIgnoreCase(authentication.getName())){
            throw new AjoException("User Not Authorized!");
        }
        else {
            throw new AjoException("Currency Not Found");
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean deleteCurrency(List<IdDTO> idDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        idDTO.forEach(idDTO1 ->{
            Optional<Currency> currency = currencyRepository.findById(idDTO1.getId());
            if (currency.isPresent()){
                Currency oldCurrency  = (Currency) SerializationUtils.clone(currency.get());
                currency.get().setStatus(Constants.MasterDataStatus.WAITING_FOR_DELETE_APPROVAL);
                currencyRepository.save(currency.get());

                //TODO AUDIT TRAIL DELETE CURRENCY BUT WAITING APPROVAL --DONE
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.CURRENCY,
                        new JSONObject(oldCurrency).toString(),
                        new JSONObject(currency.get()).toString(),
                        Constants.Remark.DELETE_CURRENCY_WAITING_APPROVAL,
                        userLogin );
            }else {
                throw new AjoException("Id "+idDTO1.getId()+" Currency Not found");
            }
        });
        return Boolean.TRUE;
    }
}
