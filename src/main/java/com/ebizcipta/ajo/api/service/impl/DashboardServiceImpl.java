package com.ebizcipta.ajo.api.service.impl;

import ch.qos.logback.core.status.StatusUtil;
import com.ebizcipta.ajo.api.domain.*;
import com.ebizcipta.ajo.api.repositories.*;
import com.ebizcipta.ajo.api.service.DashboardService;
import com.ebizcipta.ajo.api.service.dto.DashboardDTO;
import com.ebizcipta.ajo.api.service.dto.DashboardITDTO;
import com.ebizcipta.ajo.api.util.Constants;
import com.ebizcipta.ajo.api.util.Statusutil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class DashboardServiceImpl implements DashboardService{

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Statusutil statusutil;
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private BeneficiaryRepository beneficiaryRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private JenisJaminanRepository jenisJaminanRepository;
    @Autowired
    private JenisProdukRepository jenisProdukRepository;
    @Autowired
    private UnitPenggunaRepository unitPenggunaRepository;
    @Autowired
    private AlamatBankPenerbitRepository alamatBankPenerbitRepository;
    @Autowired
    private MasterConfigurationRepository masterConfigurationRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<DashboardITDTO> getCountByStatus(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DashboardITDTO dashboardITDTO = new DashboardITDTO();
        Optional<Role> roleFound = roleRepository.findByNameLikeIgnoreCase(authentication.getAuthorities().stream().findFirst().get().toString());
        if (roleFound.isPresent()){
            dashboardITDTO = findDashboard(roleFound);
        }
        return Optional.ofNullable(dashboardITDTO);
    }

    public DashboardITDTO findDashboard(Optional<Role> role){
        List<String> listStatusUser = statusutil.findStatusBasedOnRole(role.get());
        List<String> listMasterStatus = statusutil.getStatusForMasterDataRoleIt();
        DashboardITDTO dashboardITDTO = new DashboardITDTO();
        if (role.get().getCode().equalsIgnoreCase(Constants.Role.IT)){
            dashboardITDTO.setDashboardUser(getDashboardUserAccess(listStatusUser, role));
            dashboardITDTO.setDashboardAlamatBankPenerbit(getDashboardAlamatBankPenerbit(listMasterStatus, role));
            dashboardITDTO.setDashboardBeneficiary(getDashboardBeneficiary(listMasterStatus, role));
            dashboardITDTO.setDashboardCurrency(getDashboardCurrency(listMasterStatus, role));
            dashboardITDTO.setDashboardJenisJaminan(getDashboardJenisJaminan(listMasterStatus, role));
            dashboardITDTO.setDashboardJenisProduk(getDashboardJenisProduk(listMasterStatus, role));
            dashboardITDTO.setDashboardUnitPengguna(getDashboardUnitPengguna(listMasterStatus, role));
            dashboardITDTO.setDashboardMasterConfiguration(getDashboardMasterConfigurasi(listMasterStatus, role));
        }else if (role.get().getCode().equalsIgnoreCase(Constants.Role.TRO_MAKER)
                || role.get().getCode().equalsIgnoreCase(Constants.Role.TRO_CHECKER)
                || role.get().getCode().equalsIgnoreCase(Constants.Role.BENEFICIARY_USER)){
            dashboardITDTO.setDashboardBankGaransi(getDashboardBankGaransi(listStatusUser, role));
        }else if (role.get().getCode().equalsIgnoreCase(Constants.Role.BANK_ADMIN_1_MAKER)
                || role.get().getCode().equalsIgnoreCase(Constants.Role.BANK_ADMIN_1_CHECKER)){
            dashboardITDTO.setDashboardUser(getDashboardUserAccess(listStatusUser, role));
            dashboardITDTO.setDashboardUserGroup(getDashboardUserGroup(listStatusUser, role));
        }else {
            dashboardITDTO.setDashboardUser(getDashboardUserAccess(listStatusUser, role));
        }
        return dashboardITDTO;
    }

    public List<DashboardDTO> getDashboardUserAccess(List<String> status, Optional<Role> role){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userFound = userRepository.findOneByUsernameAndIsEnabled(authentication.getName(),true);
        List<DashboardDTO> dashboardDTOUser = new ArrayList<>();
        for (int i=0; i<status.size();i++){
            List<User> users = null;
            DashboardDTO dashboardDTO = new DashboardDTO();
            if (role.get().getCode().equalsIgnoreCase(Constants.Role.IT)){
                users = userRepository.getUserListITDashboard(role.get().getCode(), status.get(i), authentication.getName());
            }else if(role.get().getCode().equalsIgnoreCase(Constants.Role.BANK_ADMIN_2_MAKER)
                    || role.get().getCode().equalsIgnoreCase(Constants.Role.BANK_ADMIN_2_CHECKER)){
                users = userRepository.getUserListGlobalDashboard(Constants.Role.BANK_ADMIN_2_MAKER, status.get(i));
            }else if(role.get().getCode().equalsIgnoreCase(Constants.Role.BENEFICIARY_ADMIN_1)
                    || role.get().getCode().equalsIgnoreCase(Constants.Role.BENEFICIARY_ADMIN_2)){
                users = userRepository.getUserListBeneficiaryDashboard(role.get().getCode(), userFound.get().getBeneficiary(), status.get(i));
            }else if (role.get().getCode().equalsIgnoreCase(Constants.Role.BANK_ADMIN_1_MAKER)
                    || role.get().getCode().equalsIgnoreCase(Constants.Role.BANK_ADMIN_1_CHECKER)){
                users = userRepository.getUserListGlobalDashboard(Constants.Role.BANK_ADMIN_1_MAKER, status.get(i));
            }else {
                users = userRepository.getUserListGlobalDashboard(role.get().getCode(), status.get(i));
            }
            if (users.size() > 0){
                dashboardDTO.setStatus(status.get(i));
                dashboardDTO.setJumlah(String.valueOf(users.size()));
                dashboardDTOUser.add(dashboardDTO);
            }
        }
        return  dashboardDTOUser;
    }


    public List<DashboardDTO> getDashboardUserGroup(List<String> status, Optional<Role> role){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<DashboardDTO> dashboardDTOUserGroup = new ArrayList<>();
        for (int i=0; i<status.size();i++){
            DashboardDTO dashboardDTO = new DashboardDTO();
            List<Role> userGroups = roleRepository.findByStatusAndModifiedByNot(status.get(i), authentication.getName());
            if (userGroups.size() > 0){
                dashboardDTO.setStatus(status.get(i));
                dashboardDTO.setJumlah(String.valueOf(userGroups.size()));
                dashboardDTOUserGroup.add(dashboardDTO);
            }
        }
        return dashboardDTOUserGroup;
    }

    public List<DashboardDTO> getDashboardAlamatBankPenerbit(List<String> status, Optional<Role> role){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<DashboardDTO> dashboardDTOAlamatBankPenerbit = new ArrayList<>();
        for (int i=0; i<status.size();i++){
            DashboardDTO dashboardDTO = new DashboardDTO();
            List<AlamatBankPenerbit> alamatBankPenerbits = alamatBankPenerbitRepository.findByStatusAndModifiedByNot(status.get(i), authentication.getName());
            if (alamatBankPenerbits.size() > 0){
                dashboardDTO.setStatus(status.get(i));
                dashboardDTO.setJumlah(String.valueOf(alamatBankPenerbits.size()));
                dashboardDTOAlamatBankPenerbit.add(dashboardDTO);
            }
        }
        return dashboardDTOAlamatBankPenerbit;
    }

    public List<DashboardDTO> getDashboardBeneficiary(List<String> status, Optional<Role> role){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<DashboardDTO> dashboardDTOBeneficiary = new ArrayList<>();
        for (int i=0; i<status.size();i++){
            DashboardDTO dashboardDTO = new DashboardDTO();
            List<Beneficiary> beneficiaries = beneficiaryRepository.findByStatusAndModifiedByNot(status.get(i), authentication.getName());
            if (beneficiaries.size() > 0){
                dashboardDTO.setStatus(status.get(i));
                dashboardDTO.setJumlah(String.valueOf(beneficiaries.size()));
                dashboardDTOBeneficiary.add(dashboardDTO);
            }
        }
        return dashboardDTOBeneficiary;
    }

    public List<DashboardDTO> getDashboardCurrency(List<String> status, Optional<Role> role){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<DashboardDTO> dashboardDTOCurrency = new ArrayList<>();
        for (int i=0; i<status.size();i++){
            DashboardDTO dashboardDTO = new DashboardDTO();
            List<Currency> currencies = currencyRepository.findByStatusAndModifiedByNot(status.get(i), authentication.getName());
            if (currencies.size() > 0){
                dashboardDTO.setStatus(status.get(i));
                dashboardDTO.setJumlah(String.valueOf(currencies.size()));
                dashboardDTOCurrency.add(dashboardDTO);
            }
        }
        return dashboardDTOCurrency;
    }

    public List<DashboardDTO> getDashboardJenisJaminan(List<String> status, Optional<Role> role){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<DashboardDTO> dashboardDTOJenisJaminan = new ArrayList<>();
        for (int i=0; i<status.size();i++){
            DashboardDTO dashboardDTO = new DashboardDTO();
            List<JenisJaminan> jenisJaminans = jenisJaminanRepository.findByStatusAndModifiedByNot(status.get(i), authentication.getName());
            if (jenisJaminans.size() > 0){
                dashboardDTO.setStatus(status.get(i));
                dashboardDTO.setJumlah(String.valueOf(jenisJaminans.size()));
                dashboardDTOJenisJaminan.add(dashboardDTO);
            }
        }
        return dashboardDTOJenisJaminan;
    }

    public List<DashboardDTO> getDashboardJenisProduk(List<String> status, Optional<Role> role){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<DashboardDTO> dashboardDTOJenisProduk = new ArrayList<>();
        for (int i=0; i<status.size();i++){
            DashboardDTO dashboardDTO = new DashboardDTO();
            List<JenisProduk> jenisProduks = jenisProdukRepository.findByStatusAndModifiedByNot(status.get(i), authentication.getName());
            if (jenisProduks.size() > 0){
                dashboardDTO.setStatus(status.get(i));
                dashboardDTO.setJumlah(String.valueOf(jenisProduks.size()));
                dashboardDTOJenisProduk.add(dashboardDTO);
            }
        }
        return dashboardDTOJenisProduk;
    }

    public List<DashboardDTO> getDashboardUnitPengguna(List<String> status, Optional<Role> role){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<DashboardDTO> dashboardDTOUnitPengguna = new ArrayList<>();
        for (int i=0; i<status.size();i++){
            DashboardDTO dashboardDTO = new DashboardDTO();
            List<UnitPengguna> unitPenggunas = unitPenggunaRepository.findByStatusAndModifiedByNot(status.get(i), authentication.getName());
            if (unitPenggunas.size() > 0){
                dashboardDTO.setStatus(status.get(i));
                dashboardDTO.setJumlah(String.valueOf(unitPenggunas.size()));
                dashboardDTOUnitPengguna.add(dashboardDTO);
            }
        }
        return dashboardDTOUnitPengguna;
    }

    public List<DashboardDTO> getDashboardMasterConfigurasi(List<String> status, Optional<Role> role){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<DashboardDTO> dashboardDTOMasterConfiguration = new ArrayList<>();
        for (int i=0; i<status.size();i++){
            DashboardDTO dashboardDTO = new DashboardDTO();
            List<MasterConfiguration> masterConfigurations = masterConfigurationRepository.findByStatusAndModifiedByNot(status.get(i), authentication.getName());
            if (masterConfigurations.size() > 0){
                dashboardDTO.setStatus(status.get(i));
                dashboardDTO.setJumlah(String.valueOf(masterConfigurations.size()));
                dashboardDTOMasterConfiguration.add(dashboardDTO);
            }
        }
        return dashboardDTOMasterConfiguration;
    }

    public List<DashboardDTO> getDashboardBankGaransi(List<String> status, Optional<Role> role){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<DashboardDTO> dashboardDTOBankGaransi = new ArrayList<>();
        for (int i=0; i<status.size();i++){
            DashboardDTO dashboardDTO = new DashboardDTO();
            List<Registration> registrations = registrationRepository.findByBgStatus(status.get(i));
            if (registrations.size() > 0){
                dashboardDTO.setStatus(status.get(i));
                dashboardDTO.setJumlah(String.valueOf(registrations.size()));
                dashboardDTOBankGaransi.add(dashboardDTO);
            }
        }
        return dashboardDTOBankGaransi;
    }

}
