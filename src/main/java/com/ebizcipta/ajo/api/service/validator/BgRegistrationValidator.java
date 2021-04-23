package com.ebizcipta.ajo.api.service.validator;

import com.ebizcipta.ajo.api.domain.BankGuaranteeTxnmTemp;
import com.ebizcipta.ajo.api.domain.Beneficiary;
import com.ebizcipta.ajo.api.domain.Registration;
import com.ebizcipta.ajo.api.exception.BadRequestAlertException;
import com.ebizcipta.ajo.api.repositories.RegistrationRepository;
import com.ebizcipta.ajo.api.service.dto.ApprovalDTO;
import com.ebizcipta.ajo.api.service.dto.RegistrationDTO;
import com.ebizcipta.ajo.api.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BgRegistrationValidator {
    @Autowired
    RegistrationRepository registrationRepository;

    public Boolean checkRegistration(RegistrationDTO registrationDTO){
        Boolean result = Boolean.FALSE;
        Optional<Registration> registration = registrationRepository.findById(registrationDTO.getId());

        if (registration.get().getBgStatus().toUpperCase().equalsIgnoreCase(Constants.BankGuaranteeStatus.DRAFT) || registration.get().getBgStatus().toUpperCase().equalsIgnoreCase(Constants.BankGuaranteeStatus.BGFROMSTAGING) || registration.get().getBgStatus().toUpperCase().equalsIgnoreCase(Constants.BankGuaranteeStatus.REJECT)){
            result = Boolean.TRUE;
        }else {
                throw new BadRequestAlertException("You Cannot Do Something While Status Is Not Valid", "", "");
        }
        return result;
    }

    public String emptyValTempToRegistration(BankGuaranteeTxnmTemp bankGuaranteeTxnmTemp, List<Beneficiary> beneficiary){
        String result = null;
        for (int i=0; i<beneficiary.size();i++){
            if (bankGuaranteeTxnmTemp.getC091_TXNM_BEN_NAME().contains(beneficiary.get(i).getCodeBeneficiary())){
                result = beneficiary.get(i).getCodeBeneficiary();
                break;
            }
        }
        return result;
    }

    public Boolean approvalVerifiedSettleActionValidation(ApprovalDTO approvalDTO, Optional<Registration> registration){
//        Boolean result = Boolean.FALSE;
//        if (registration.isPresent()){
//            if (approvalDTO.getAction().toUpperCase().equalsIgnoreCase(Constants.registrationAction.APPROVE)
//                    && !registration.get().getBgStatus().equalsIgnoreCase(Constants.BankGuaranteeStatus.WAITINGBGAPPROVAL)){
//                throw new BadRequestAlertException("Approval hanya berlaku jika status = waiting for approval","","");
//            }else if (approvalDTO.getAction().toUpperCase().equalsIgnoreCase(Constants.registrationAction.VERIFIKASI)){
//                if (registration.get().getBgStatus().equalsIgnoreCase(Constants.BankGuaranteeStatus.APPROVEDBG)
//                        || registration.get().getBgStatus().equalsIgnoreCase(Constants.BankGuaranteeStatus.WAITINGCHECKERVERIFICATION)
//                        || registration.get().getBgStatus().equalsIgnoreCase(Constants.BankGuaranteeStatus.WAITINGBGVERIFICATION)){
//                    result = Boolean.TRUE;
//                }else {
//                    throw new BadRequestAlertException("Verifikasi hanya berlaku jika status = waiting for verifikasi","","");
//                }
//            }else if (approvalDTO.getAction().toUpperCase().equalsIgnoreCase(Constants.registrationAction.SETTLEMENT)){
//                if (registration.get().getBgStatus().equalsIgnoreCase(Constants.BankGuaranteeStatus.VERIFIEDBG)
//                        || registration.get().getBgStatus().equalsIgnoreCase(Constants.BankGuaranteeStatus.WAITINGBGSETTLEMENT)){
//                    result = Boolean.TRUE;
//                }else {
//                    throw new BadRequestAlertException("Settlement hanya berlaku jika bg sudah di verifikasi","","");
//                }
//            }else if (approvalDTO.getAction().toUpperCase().equalsIgnoreCase(Constants.registrationAction.REJECT)
//                    && registration.get().getBgStatus().equalsIgnoreCase(Constants.BankGuaranteeStatus.REJECT)){
//                throw new BadRequestAlertException("BG Dengan Status "+registration.get().getBgStatus()+" Tidak Dapat di Reject","","");
//            }
//            else {
//                result = Boolean.TRUE;
//            }
//        }else {
//            throw new BadRequestAlertException("Nomor Jaminan Tidak ditemukan","","");
//        }
        return  Boolean.TRUE;
    }
}
