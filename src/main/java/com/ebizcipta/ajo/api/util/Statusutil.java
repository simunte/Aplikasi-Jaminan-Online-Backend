package com.ebizcipta.ajo.api.util;

import com.ebizcipta.ajo.api.domain.Registration;
import com.ebizcipta.ajo.api.domain.Role;
import com.ebizcipta.ajo.api.domain.User;
import com.ebizcipta.ajo.api.repositories.RegistrationRepository;
import com.ebizcipta.ajo.api.repositories.UserRepository;
import com.ebizcipta.ajo.api.service.dto.DashboardDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@PropertySource("classpath:data.properties")
public class Statusutil {
    @Value("${role.nsb.name}")
    private String roleNasabah;

    @Value("${role.tro.maker.name}")
    private String roleTroMaker;

    @Value("${role.tro.checker.name}")
    private String roleTroChecker;

    @Value("${role.reviewer.name}")
    private String roleKomite;

    @Value("${role.beneficiary.user.name}")
    private String roleBeneficiaryUser;

    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private UserRepository userRepository;

    public static List<String> getStatusRoleTroMaker(){
        return Arrays.asList(
                Constants.BankGuaranteeStatus.BGFROMSTAGING,
                Constants.BankGuaranteeStatus.DRAFT,
                Constants.BankGuaranteeStatus.REJECT,
                Constants.BankGuaranteeStatus.WAITINGBGAPPROVAL);
    }

    public static List<String> getStatusRoleTroChecker(){
        return Arrays.asList(
                Constants.BankGuaranteeStatus.WAITINGBGVERIFICATION,
                Constants.BankGuaranteeStatus.APPROVEDBG);
    }

    public static List<String> getStatusRoleTroCheckerDeleteBg(){
        return Arrays.asList(
                Constants.BankGuaranteeStatus.BGFROMSTAGING,
                Constants.BankGuaranteeStatus.DRAFT,
                Constants.BankGuaranteeStatus.WAITINGBGAPPROVAL,
                Constants.BankGuaranteeStatus.REJECT);
    }

    public static List<String> getStatusRolePlnUser(){
        return Arrays.asList(
                Constants.BankGuaranteeStatus.WAITINGBGCONFIRMATION,
                Constants.BankGuaranteeStatus.APPROVEDBG);
    }

    public static List<String> getStatusRolePlnUserRekapitulasi(){
        return Arrays.asList(
                Constants.BankGuaranteeStatus.APPROVEDBG,
                Constants.BankGuaranteeStatus.WAITINGBGVERIFICATION,
                Constants.BankGuaranteeStatus.WAITINGBGCONFIRMATION,
                Constants.BankGuaranteeStatus.SETTLEDBG);
    }

    public static List<String> getStatusRolePlnUserVerifikasi(){
        return Arrays.asList(
                Constants.BankGuaranteeStatus.WAITINGBGCONFIRMATION,
                Constants.BankGuaranteeStatus.WAITINGBGVERIFICATION);
    }

    public static List<String> getStatusRolePlnUserSettlement(){
        return Arrays.asList(
                Constants.BankGuaranteeStatus.APPROVEDBG,
                Constants.BankGuaranteeStatus.WAITINGBGSETTLEMENT,
                Constants.BankGuaranteeStatus.SETTLEDBG);
    }

    public static List<String> getStatusCheckerSettlement(){
        return Arrays.asList(
                Constants.BankGuaranteeStatus.APPROVEDBG,
                Constants.BankGuaranteeStatus.WAITINGBGSETTLEMENT);
    }

    public static List<String> getStatusManualDataTransfer(){
        return Arrays.asList(
                Constants.BankGuaranteeStatus.APPROVEDBG,
                Constants.BankGuaranteeStatus.FAILED_BG);
    }

    public static List<String> getStatusBankGuaranteeKomite(){
        return Arrays.asList(
                Constants.BankGuaranteeStatus.WAITINGBGVALIDATION,
                Constants.BankGuaranteeStatus.APPROVEDBG,
                Constants.BankGuaranteeStatus.SETTLEDBG);
    }

    public static List<String> getStatusBankGuaranteeTROMaker(){
        return Arrays.asList(
                Constants.BankGuaranteeStatus.BGFROMSTAGING,
                Constants.BankGuaranteeStatus.DRAFT,
                Constants.BankGuaranteeStatus.REJECT,
                Constants.BankGuaranteeStatus.WAITINGBGAPPROVAL,
                Constants.BankGuaranteeStatus.SETTLEDBG);
    }

    public static List<String> getStatusBankGuaranteeTROChecker(){
        return Arrays.asList(
                Constants.BankGuaranteeStatus.WAITINGBGVERIFICATION,
                Constants.BankGuaranteeStatus.APPROVEDBG,
                Constants.BankGuaranteeStatus.SETTLEDBG);
    }

    public static List<String> getStatusBankGuaranteeBeneficiaryUSser(){
        return Arrays.asList(
                Constants.BankGuaranteeStatus.WAITINGBGCONFIRMATION,
                Constants.BankGuaranteeStatus.APPROVEDBG,
                Constants.BankGuaranteeStatus.SETTLEDBG);
    }

    public static List<String> getStatusForUserRoleUserMaker(){
        return Arrays.asList(
                Constants.UserStatus.LOCKED,
                Constants.UserStatus.REJECT_NEW);
    }
    public static List<String> getStatusForUserRoleUserChecker(){
        return Arrays.asList(
                Constants.UserStatus.CREATE_NEW,
                Constants.UserStatus.REQ_RESET_PASSWORD,
                Constants.UserStatus.LOCKED,
                Constants.UserStatus.REQ_DELETED);
    }
    public static List<String> getStatusForUserRoleUserIT(){
        return Arrays.asList(
                Constants.UserStatus.CREATE_NEW,
                Constants.UserStatus.REJECT_NEW,
                Constants.UserStatus.REQ_RESET_PASSWORD,
                Constants.UserStatus.LOCKED,
                Constants.UserStatus.REQ_DELETED);
    }

    public static List<String> getStatusForMasterDataRoleIt(){
        return Arrays.asList(
                Constants.MasterDataStatus.WAITING_FOR_APPROVAL,
                Constants.MasterDataStatus.REJECT,
                Constants.MasterDataStatus.WAITING_FOR_DELETE_APPROVAL);
    }

    public static List<String> getStatusForMasterDataUpdate(){
        return Arrays.asList(
                Constants.MasterDataStatus.INACTIVE);
    }

    public static List<String> getStatusBeneficiary(){
        return Arrays.asList(
                Constants.MasterDataStatus.ACTIVAT);
    }

    public List<String> findStatusBasedOnRole(Role role){
        if (role.getCode().toUpperCase().equalsIgnoreCase(roleTroMaker)){
            return getStatusRoleTroMaker();
        }else if (role.getCode().toUpperCase().equalsIgnoreCase(roleTroChecker)){
            return getStatusRoleTroChecker();
        }else if (role.getCode().toUpperCase().equalsIgnoreCase(roleBeneficiaryUser)){
            return getStatusRolePlnUser();
        }else if (role.getCode().toUpperCase().equalsIgnoreCase(Constants.Role.BANK_ADMIN_1_MAKER)
                || role.getCode().toUpperCase().equalsIgnoreCase(Constants.Role.BANK_ADMIN_2_MAKER)
                || role.getCode().toUpperCase().equalsIgnoreCase(Constants.Role.BENEFICIARY_ADMIN_1)){
            return getStatusForUserRoleUserMaker();
        }else if (role.getCode().toUpperCase().equalsIgnoreCase(Constants.Role.BANK_ADMIN_1_CHECKER)
                || role.getCode().toUpperCase().equalsIgnoreCase(Constants.Role.BANK_ADMIN_2_CHECKER)
                || role.getCode().toUpperCase().equalsIgnoreCase(Constants.Role.BENEFICIARY_ADMIN_2)){
            return getStatusForUserRoleUserChecker();
        }else {
            return getStatusForUserRoleUserIT();
        }
    }

    public List<String> findStatusBasedOnRoleForList(Role role){
        if (role.getCode().toUpperCase().equalsIgnoreCase(roleBeneficiaryUser)){
            return getStatusBankGuaranteeBeneficiaryUSser();
        }else if(role.getCode().toUpperCase().equalsIgnoreCase(roleTroMaker)){
            return getStatusBankGuaranteeTROMaker();
        }else if(role.getCode().toUpperCase().equalsIgnoreCase(roleTroChecker)){
            return getStatusBankGuaranteeTROChecker();
        }else if(role.getCode().toUpperCase().equalsIgnoreCase(roleKomite)){
            return getStatusBankGuaranteeKomite();
        }else {
            return null;
        }
    }
}
