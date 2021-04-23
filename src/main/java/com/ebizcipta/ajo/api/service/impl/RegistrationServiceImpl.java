package com.ebizcipta.ajo.api.service.impl;

import com.ebizcipta.ajo.api.domain.*;
import com.ebizcipta.ajo.api.domain.Currency;
import com.ebizcipta.ajo.api.exception.AjoException;
import com.ebizcipta.ajo.api.exception.BadRequestAlertException;
import com.ebizcipta.ajo.api.repositories.*;
import com.ebizcipta.ajo.api.service.RegistrationService;
import com.ebizcipta.ajo.api.service.dto.*;
import com.ebizcipta.ajo.api.service.mapper.RegistrationMapper;
import com.ebizcipta.ajo.api.service.mapper.ViewRegistrationMapper;
import com.ebizcipta.ajo.api.service.validator.BgRegistrationValidator;
import com.ebizcipta.ajo.api.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationUtils;
import org.apache.tomcat.util.bcel.Const;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class RegistrationServiceImpl implements RegistrationService {
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private JenisProdukRepository jenisProdukRepository;
    @Autowired
    private JenisJaminanRepository jenisJaminanRepository;
    @Autowired
    private BeneficiaryRepository beneficiaryRepository;
    @Autowired
    private UnitPenggunaRepository unitPenggunaRepository;
    @Autowired
    private UraianPekerjaanRepository uraianPekerjaanRepository;
    @Autowired
    private AlamatBankPenerbitRepository alamatBankPenerbitRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private ConfirmationRepository confirmationRepository;
    @Autowired
    private BankGuaranteeHistoryServiceImpl bankGuaranteeHistoryService;
    @Autowired
    private BgRegistrationValidator bgRegistrationValidator;
    @Autowired
    private ConfirmationServiceImpl confirmationService;
    @Autowired
    private Statusutil statusutil;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WebServicesUtil webServicesUtil;
    @Autowired
    private CurrencyUtil currencyUtil;
    @Autowired
    private FileManagementServiceImpl fileManagementService;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private DateUtil dateUtil;
    @Autowired
    private MasterConfigurationRepository masterConfigurationRepository;
    @Autowired
    private AuditTrailUtil auditTrailUtil;

    @Override
    @Transactional(readOnly = true)
    public List<ViewRegistrationDTO> findAllBgRegistration(String status) {
        List<Registration> registrations = new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findOneByUsernameAndIsEnabled(authentication.getName(), true);
        if (status == null || status == "" || status.equalsIgnoreCase("null")){
            Role role = user.get().getRoles().get(0);
            if(role.getCode().toUpperCase().equalsIgnoreCase("NASABAH")){
                registrations = registrationRepository.findByNasabah(user.get());
            }else{
                registrations = registrationRepository.findByBgStatusIn(statusutil.findStatusBasedOnRoleForList(user.get().getRoles().get(0)));
            }
        }else {
            registrations = registrationRepository.findByBgStatus(status);
        }
        return registrations.stream()
                .map(registration -> ViewRegistrationMapper.INSTANCE.toDto(registration, new ViewRegistrationDTO()))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewRegistrationDTO> findAllBgPLN(String status, String menu) {
        List<Registration> registrations = new ArrayList<>();
        if (status == null || status == "" || status.equalsIgnoreCase("null")){
            if (menu.toUpperCase().equalsIgnoreCase(Constants.MenuReport.REKAPITULASI)){
                registrations = registrationRepository.findByBgStatusIn(Statusutil.getStatusRolePlnUserRekapitulasi());
            }else if (menu.toUpperCase().equalsIgnoreCase(Constants.MenuReport.VERIFIKASI)){
                registrations = registrationRepository.findByBgStatusIn(Statusutil.getStatusRolePlnUserVerifikasi());
            }else if (menu.toUpperCase().equalsIgnoreCase(Constants.MenuReport.SETTLEMENT)){
                registrations = registrationRepository.findByBgStatusIn(Statusutil.getStatusRolePlnUserSettlement());
            }else {
                throw new BadRequestAlertException("Menu tidak valid","","");
            }
        }else {
            registrations = registrationRepository.findByBgStatus(status);
        }
        return registrations.stream()
                .map(registration -> ViewRegistrationMapper.INSTANCE.toDto(registration, new ViewRegistrationDTO()))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ViewRegistrationDTO> findByNomorJaminan(Long idJaminan) {
        return registrationRepository.findById(idJaminan)
                .map(registration -> ViewRegistrationMapper.INSTANCE.toDto(registration, new ViewRegistrationDTO()));
    }

    @Override
    @Transactional
    public ConfirmationDTO saveRegistration(RegistrationDTO dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Registration registration = new Registration();
        String remark = Constants.Remark.UPDATE_REGISTRATION_BG_SET_BG_DRAFT;
        Registration oldRegistraion = null;
        if (dto.getId() == null){
            if (registrationRepository.findTop1ByNomorJaminanAndNomorAmentmendAndBgStatusNotOrderByNomorAmentmendDesc(dto.getNomorJaminan(), dto.getNomorAmentmend(), Constants.BankGuaranteeStatus.DELETEDBG).isPresent()){
                throw new BadRequestAlertException("Nomor Jaminan dengan nomor Ama tersebut sudah terdaftar","","");
            }
            registration = RegistrationMapper.INSTANCE.toEntity(dto, registration);
            registration.setManualBg(Boolean.TRUE);
        }else {
            Optional<Registration> registrationFound = registrationRepository.findById(dto.getId());
            if (!registrationFound.isPresent()){
                throw new BadRequestAlertException("Nomor Jaminan Tidak ditemukan","","");
            }
            oldRegistraion = (Registration) SerializationUtils.clone(registrationFound.get());
            bgRegistrationValidator.checkRegistration(dto);
            registration = RegistrationMapper.INSTANCE.toEntity(dto, registrationFound.get());
        }

        registration.setBgStatus(Constants.BankGuaranteeStatus.DRAFT);
        User user = userRepository.findOneByUsernameAndIsEnabled(dto.getApplicant(), Boolean.TRUE)
                .orElseThrow(() -> new EntityNotFoundException("User tidak ditemukan"));
        registration.setNasabah(user);
        JenisProduk jenisProduk = jenisProdukRepository.findById(dto.getJenisProdukId())
                .orElseThrow(()-> new EntityNotFoundException("Jenis Produk Not Found"));
        registration.setJenisProduk(jenisProduk);
        JenisJaminan jenisJaminan = jenisJaminanRepository.findById(dto.getJenisJaminanId())
                .orElseThrow(()->new EntityNotFoundException("Jenis Jaminan Not Found"));
        registration.setJenisJaminan(jenisJaminan);
        Beneficiary beneficiary = beneficiaryRepository.findById(dto.getBeneficiaryId())
                .orElseThrow(()-> new EntityNotFoundException("Beneficiary Not Found"));
        registration.setBeneficiary(beneficiary);
        UnitPengguna unitPengguna = unitPenggunaRepository.findById(dto.getUnitPenggunaId())
                .orElseThrow(()-> new EntityNotFoundException("Unit Pengguna Not Found"));
        registration.setUnitPengguna(unitPengguna);
        Currency currency = currencyRepository.findById(dto.getCurrencyId())
                .orElseThrow(()-> new EntityNotFoundException("Currency Not Found"));
        registration.setCurrency(currency);
        AlamatBankPenerbit alamatBankPenerbit = alamatBankPenerbitRepository.findById(dto.getAlamatBankPenerbitId())
                .orElseThrow(() -> new EntityNotFoundException("Alamat Bank Penerbit Not Found"));
        registration.setAlamatBankPenerbit(alamatBankPenerbit);
        registrationRepository.saveAndFlush(registration);

        //TODO AUDIT TRAIL
        auditTrailUtil.saveAudit(
                dto.getId() != null ? Constants.Event.UPDATE : Constants.Event.CREATE,
                Constants.Module.REGISTRATION,
                dto.getId() != null ? new JSONObject(oldRegistraion).toString() : new JSONObject(registration).toString(),
                new JSONObject(registration).toString(),
                remark,
                authentication.getName()
        );
        if (dto.getId() == null){
            Registration registrationNew = registrationRepository.findTop1ByNomorJaminanAndNomorAmentmendAndBgStatusNotOrderByNomorAmentmendDesc(registration.getNomorJaminan(), registration.getNomorAmentmend(), Constants.BankGuaranteeStatus.DELETEDBG)
                    .orElseThrow(()-> new BadRequestAlertException("Bank Guarantee Tidak ditemukan", "", ""));
            return getConfirmationDto(registrationNew);
        }
        return getConfirmationDto(registration);
    }

    @Override
    @Transactional
    public Boolean registrationApproval(ApprovalDTO approvalDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findTop1ByUsernameAndApprovedDateIsNotNullOrderByCreationDateDesc(authentication.getName())
                .orElseThrow(()-> new BadRequestAlertException("Username not found", "",""));
        Optional<Registration> registration = registrationRepository.findById(approvalDTO.getIdJaminan());
        Registration oldRegistration = (Registration) SerializationUtils.clone(registration.get());
        bgRegistrationValidator.approvalVerifiedSettleActionValidation(approvalDTO, registration);
        switch (approvalDTO.getAction().toUpperCase()){
            case Constants.registrationAction.APPROVE:
                if (!registration.get().getBgStatus().equalsIgnoreCase(Constants.BankGuaranteeStatus.WAITINGBGAPPROVAL)){
                    throw new BadRequestAlertException("Tidak Dapat APPROVED BG dengan Status" + registration.get().getBgStatus(), "", "");
                }
                registration.get().setBgStatus(Constants.BankGuaranteeStatus.WAITINGBGVERIFICATION);
                registrationRepository.save(registration.get());
                savehistoryTransaction(approvalDTO.getIdJaminan(), Constants.BankGuaranteeStatus.WAITINGBGVERIFICATION, approvalDTO.getNotes());

                //TODO AUDIT TRAIL
                auditTrailUtil.saveAudit(
                        Constants.Event.APPROVAL,
                        Constants.Module.REGISTRATION,
                        new JSONObject(oldRegistration).toString(),
                        new JSONObject(registration).toString(),
                        Constants.Remark.UPDATE_REGISTRATION_BG_SET_BG_APPROVED,
                        authentication.getName()
                );
                break;
            case Constants.registrationAction.VERIFIKASI:
                if (!registration.get().getBgStatus().equalsIgnoreCase(Constants.BankGuaranteeStatus.WAITINGBGVERIFICATION)){
                    throw new BadRequestAlertException("Tidak Dapat VERIFIKASI BG dengan Status " + registration.get().getBgStatus(), "", "");
                }
                Confirmation confirmationLet = confirmationRepository.findByRegistration(registration.get())
                        .orElseThrow(()->new BadRequestAlertException("Bank Guarantee Tidak ditemukan","",""));
                //File Without Signature
                Map<String, Object> mapFile = fileManagementService.exportSuratKonfirmasi(confirmationLet, Boolean.FALSE);
                File file = (File) mapFile.get("file");
                //File With Signature
                Map<String, Object> mapFileTtd = fileManagementService.exportSuratKonfirmasi(confirmationLet, Boolean.TRUE);
                File fileTtd = (File) mapFileTtd.get("file");

                confirmationLet.setSoftCopySuratKonfirmasi(file.getName());
                confirmationLet.setSoftCopySuratKonfirmasiWithTtd(fileTtd.getName());
                confirmationRepository.save(confirmationLet);

                registration.get().setApprovedDate(Instant.now());
                registration.get().setApprovedBy(authentication.getName());
                registration.get().setUserApprove(user);
                registration.get().setBgStatus(Constants.BankGuaranteeStatus.WAITINGBGVALIDATION);
                registrationRepository.save(registration.get());
                savehistoryTransaction(approvalDTO.getIdJaminan(), Constants.BankGuaranteeStatus.WAITINGBGVALIDATION, approvalDTO.getNotes());

                //TODO AUDIT TRAIL
                auditTrailUtil.saveAudit(
                        Constants.Event.APPROVAL,
                        Constants.Module.REGISTRATION,
                        new JSONObject(oldRegistration).toString(),
                        new JSONObject(registration).toString(),
                        Constants.Remark.UPDATE_REGISTRATION_BG_SET_BG_APPROVED,
                        authentication.getName()
                );
                break;
            case Constants.registrationAction.VALIDASI:
                if (!registration.get().getBgStatus().equalsIgnoreCase(Constants.BankGuaranteeStatus.WAITINGBGVALIDATION)){
                    throw new BadRequestAlertException("Tidak Dapat VALIDASI BG dengan Status" + registration.get().getBgStatus(), "", "");
                }
                registration.get().setBgStatus(Constants.BankGuaranteeStatus.WAITINGBGCONFIRMATION);
                registrationRepository.save(registration.get());
                savehistoryTransaction(approvalDTO.getIdJaminan(), Constants.BankGuaranteeStatus.WAITINGBGCONFIRMATION, approvalDTO.getNotes());

                //TODO AUDIT TRAIL
                auditTrailUtil.saveAudit(
                        Constants.Event.APPROVAL,
                        Constants.Module.REGISTRATION,
                        new JSONObject(oldRegistration).toString(),
                        new JSONObject(registration).toString(),
                        Constants.Remark.UPDATE_REGISTRATION_BG_SET_BG_APPROVED,
                        authentication.getName()
                );
                break;
            case Constants.registrationAction.CONFIRM:
                if (!registration.get().getBgStatus().equalsIgnoreCase(Constants.BankGuaranteeStatus.WAITINGBGCONFIRMATION)){
                    throw new BadRequestAlertException("Tidak Dapat KONFIRMASI BG dengan Status" + registration.get().getBgStatus(), "", "");
                }
                registration.get().setBgStatus(Constants.BankGuaranteeStatus.APPROVEDBG);
                registrationRepository.save(registration.get());
                savehistoryTransaction(approvalDTO.getIdJaminan(), Constants.BankGuaranteeStatus.APPROVEDBG, approvalDTO.getNotes());

                //TODO AUDIT TRAIL
                auditTrailUtil.saveAudit(
                        Constants.Event.APPROVAL,
                        Constants.Module.REGISTRATION,
                        new JSONObject(oldRegistration).toString(),
                        new JSONObject(registration).toString(),
                        Constants.Remark.UPDATE_REGISTRATION_BG_SET_BG_APPROVED,
                        authentication.getName()
                );
                break;
            case Constants.registrationAction.SETTLEMENT:
                if (!registration.get().getBgStatus().equalsIgnoreCase(Constants.BankGuaranteeStatus.SETTLEDBG)){
                    throw new BadRequestAlertException("Tidak Dapat TUTUP BG dengan Status" + registration.get().getBgStatus(), "", "");
                }
                registration.get().setBgStatus(Constants.BankGuaranteeStatus.SETTLEDBG);
                registrationRepository.save(registration.get());
                savehistoryTransaction(approvalDTO.getIdJaminan(), Constants.BankGuaranteeStatus.SETTLEDBG, approvalDTO.getNotes());

                //TODO AUDIT TRAIL
                auditTrailUtil.saveAudit(
                        Constants.Event.APPROVAL,
                        Constants.Module.REGISTRATION,
                        new JSONObject(oldRegistration).toString(),
                        new JSONObject(registration).toString(),
                        Constants.Remark.UPDATE_REGISTRATION_BG_SET_BG_APPROVED,
                        authentication.getName()
                );
                break;
            case Constants.registrationAction.REJECT:
                if (registration.get().getBgStatus().equalsIgnoreCase(Constants.BankGuaranteeStatus.REJECT)){
                    throw new BadRequestAlertException("Tidak Dapat REJECT BG dengan Status" + registration.get().getBgStatus(), "", "");
                }
                registration.get().setBgStatus(Constants.BankGuaranteeStatus.REJECT);
                registrationRepository.save(registration.get());
                savehistoryTransaction(approvalDTO.getIdJaminan(), Constants.BankGuaranteeStatus.REJECT, approvalDTO.getNotes());

                //TODO AUDIT TRAIL
                auditTrailUtil.saveAudit(
                        Constants.Event.APPROVAL,
                        Constants.Module.REGISTRATION,
                        new JSONObject(oldRegistration).toString(),
                        new JSONObject(registration).toString(),
                        Constants.Remark.UPDATE_REGISTRATION_BG_SET_BG_APPROVED,
                        authentication.getName()
                );
                break;
            default:
                throw new BadRequestAlertException("Action tidak valid","","");
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean submitDraft(Long idJaminan, Boolean draft){
        Optional<Registration> registration = registrationRepository.findById(idJaminan);
        String status;
        if (registration.isPresent()){
            if (draft){
                registration.get().setBgStatus(Constants.BankGuaranteeStatus.DRAFT);
                savehistoryTransaction(idJaminan, Constants.BankGuaranteeStatus.DRAFT, "");
            }else {
                registration.get().setBgStatus(Constants.BankGuaranteeStatus.WAITINGBGAPPROVAL);
                savehistoryTransaction(idJaminan, Constants.BankGuaranteeStatus.WAITINGBGAPPROVAL, "");
            }
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BankGuaranteeDTO> getDetailBankGuarantee(Long id_jaminan){
        Optional<Registration> registrationFound = registrationRepository.findById(id_jaminan);
        BankGuaranteeDTO bankGuaranteeDTO = new BankGuaranteeDTO();
        Optional<ViewRegistrationDTO> registrationDTO = findByNomorJaminan(id_jaminan);
        Optional<ViewConfirmationDTO> confirmationDTO = confirmationService.findConfirmationByNomorJaminan(registrationFound.get().getId());
        if (registrationDTO.isPresent()){
            bankGuaranteeDTO.setRegistrationDTO(ViewRegistrationMapper.INSTANCE.registrationToBankGuaranteeDTO(registrationDTO.get(), new ViewRegistrationDTO()));
        }else {
            bankGuaranteeDTO.setRegistrationDTO(new ViewRegistrationDTO());
        }
        if (confirmationDTO.isPresent()){
            bankGuaranteeDTO.setConfirmationDTO(ViewRegistrationMapper.INSTANCE.confirmationToBankGuaranteeDTO(confirmationDTO.get(), new ViewConfirmationDTO()));
        }else {
            String nomorKonfirmasi = null;
            if (registrationFound.get().getId().toString().length() < 3){
                String noll = "0";
                String fixEmpty = "";
                Integer length = registrationFound.get().getId().toString().length();
                while (length < 3){
                    fixEmpty = fixEmpty.concat(noll);
                    length++;
                }
                nomorKonfirmasi = "2019/NTRO-BG/"+fixEmpty+registrationFound.get().getId().toString();
            }else {
                nomorKonfirmasi = "2019/NTRO-BG/"+registrationFound.get().getId().toString();
            }
            ViewConfirmationDTO viewConfirmationDTO = new ViewConfirmationDTO();
            viewConfirmationDTO.setNomorKonfirmasiBank(nomorKonfirmasi);
            bankGuaranteeDTO.setConfirmationDTO(viewConfirmationDTO);
        }
        return Optional.ofNullable(bankGuaranteeDTO);
    }

    @Override
    @Transactional
    public Boolean deleteBankGuarantee(List <IdDTO> idDTOS){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        idDTOS.forEach(idDTO1 ->{
            Optional<Registration> registration = registrationRepository.findById(idDTO1.getId());
            if (registration.isPresent() && Statusutil.getStatusRoleTroCheckerDeleteBg().contains(registration.get().getBgStatus())){
                Registration oldRegistration = (Registration) SerializationUtils.clone(registration.get());
                registration.get().setBgStatus(Constants.BankGuaranteeStatus.DELETEDBG);
                registrationRepository.save(registration.get());
                savehistoryTransaction(registration.get().getId(), Constants.BankGuaranteeStatus.DELETEDBG, "");

                //TODO AUDIT TRAIL
                auditTrailUtil.saveAudit(
                        Constants.Event.DELETE,
                        Constants.Module.REGISTRATION,
                        new JSONObject(oldRegistration).toString(),
                        new JSONObject(registration).toString(),
                        Constants.Remark.UPDATE_REGISTRATION_BG_SET_BG_DELETED,
                        authentication.getName()
                );
            }else {
                throw new AjoException("Nomor Jaminan Tidak Ditemukan atau Status tidak valid");
            }
        });
        return Boolean.TRUE;
    }

    private void savehistoryTransaction(Long id_jaminan, String status, String notes){
        BankGuaranteeHistoryDTO dtoHistory = new BankGuaranteeHistoryDTO();
        dtoHistory.setId(0L);
        dtoHistory.setIdJaminan(id_jaminan);
        dtoHistory.setBgStatus(status);
        dtoHistory.setNotes(notes);
        bankGuaranteeHistoryService.saveHistory(dtoHistory);
    }

    @Async
    Boolean sendEmailBeneficiaryRejected(Optional<Registration> registration, String notes){
        Optional<MasterConfiguration> masterConfiguration = masterConfigurationRepository.findTop1ByStatusOrderByCreationDateDesc("ACTIVE");
        EmailRejectDTO emailRejectDTO = new EmailRejectDTO();
        emailRejectDTO.setSetFrom(masterConfiguration.get().getUsername());
        emailRejectDTO.setRecipient(masterConfiguration.get().getMailingList());
        emailRejectDTO.setSubject("REJECTED BG BY PLN");
        emailRejectDTO.setNamaPemohon(registration.get().getApplicant());
        emailRejectDTO.setNoKontrak(registration.get().getNomorKontrak());
        emailRejectDTO.setNoJaminan(registration.get().getNomorJaminan());
        emailRejectDTO.setTanggalTerbit(dateUtil.instantToString(dateUtil.longToInstant(registration.get().getTanggalTerbit()), "dd-MM-yyyy"));
        emailRejectDTO.setTanggalBerlaku(dateUtil.instantToString(dateUtil.longToInstant(registration.get().getTanggalBerlaku()),"dd-MM-yyyy"));
        emailRejectDTO.setTanggalJatuhTempo(dateUtil.instantToString(dateUtil.longToInstant(registration.get().getTanggalBatasClaim()),"dd-MM-yyyy"));
        emailRejectDTO.setCurrency(registration.get().getCurrency().getCurrency());
        emailRejectDTO.setNilaiJaminan(currencyUtil.currencyFormatting(registration.get().getNilaiJaminan()));
        emailRejectDTO.setCatatan(notes);
        Boolean result = emailUtil.sendEmail(emailRejectDTO);
        if (!result){
            log.info("Gagal Mengirimkan Email ke Mailing List Operation");
        }
        return Boolean.TRUE;
    }

    private String settledAll(Registration registration){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String result = registration.getBgStatus();
        Registration registrationTop = registrationRepository.findTop1ByNomorJaminanAndBgStatusInOrderByNomorAmentmendDesc(registration.getNomorJaminan(), Statusutil.getStatusCheckerSettlement())
                .orElseThrow(()->new BadRequestAlertException("Nomor Jaminan Tidak ditemukan","",""));
        if (registration.getNomorAmentmend().equalsIgnoreCase(registrationTop.getNomorAmentmend())){
            Registration oldRegistration = (Registration) SerializationUtils.clone(registration);
            List<Registration> registrationAll = registrationRepository.findByNomorJaminanAndBgStatusNot(registration.getNomorJaminan(), Constants.BankGuaranteeStatus.DELETEDBG);
            registrationAll.forEach(x -> {
                x.setBgStatus(Constants.BankGuaranteeStatus.SETTLEDBG);
                registrationRepository.save(x);
            });
            result = Constants.BankGuaranteeStatus.SETTLEDBG;

            //TODO AUDIT TRAIL
            auditTrailUtil.saveAudit(
                    Constants.Event.SETTLEMENT,
                    Constants.Module.REGISTRATION,
                    new JSONObject(oldRegistration).toString(),
                    new JSONObject(registration).toString(),
                    Constants.Remark.UPDATE_REGISTRATION_BG_SET_BG_SETTLED,
                    authentication.getName()
            );
        }else {
            throw new BadRequestAlertException("Settle Hanya Berlaku Untuk AMA Terakhir","","");
        }
        return result;
    }

    private ConfirmationDTO getConfirmationDto(Registration registrationFound){
        ConfirmationDTO confirmationDTO = new ConfirmationDTO();
        String nomorKonfirmasi = null;
        if (registrationFound.getId().toString().length() < 3){
            String noll = "0";
            String fixEmpty = "";
            Integer length = registrationFound.getId().toString().length();
            while (length < 3){
                fixEmpty = fixEmpty.concat(noll);
                length++;
            }
            nomorKonfirmasi = "2019/NTRO-BG/"+fixEmpty+registrationFound.getId().toString();
        }else {
            nomorKonfirmasi = "2019/NTRO-BG/"+registrationFound.getId().toString();
        }
        confirmationDTO.setIdJaminan(registrationFound.getId());
        confirmationDTO.setNomorKonfirmasiBank(nomorKonfirmasi);
        return confirmationDTO;
    }
}
