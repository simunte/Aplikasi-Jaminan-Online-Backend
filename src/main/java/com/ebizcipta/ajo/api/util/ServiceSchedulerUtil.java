package com.ebizcipta.ajo.api.util;

import com.ebizcipta.ajo.api.domain.Registration;
import com.ebizcipta.ajo.api.domain.User;
import com.ebizcipta.ajo.api.exception.BadRequestAlertException;
import com.ebizcipta.ajo.api.repositories.RegistrationRepository;
import com.ebizcipta.ajo.api.repositories.UserRepository;
import com.ebizcipta.ajo.api.service.dto.BankGuaranteeHistoryDTO;
import com.ebizcipta.ajo.api.service.impl.BankGuaranteeHistoryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.Period;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@Configuration
@EnableScheduling
@PropertySource("classpath:configuration/web-service.properties")
public class ServiceSchedulerUtil{
    @Autowired
    private WebServicesUtil webServicesUtil;
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DateUtil dateUtil;
    @Autowired
    private BankGuaranteeHistoryServiceImpl bankGuaranteeHistoryService;
    @Autowired
    private AuditTrailUtil auditTrailUtil;

    @Value("${cron.string.scheduler}")
    String cronScheduler;

    @Scheduled(cron = Constants.setUpConfiguration.CRON_SCHEDULER)
    public Boolean sendToABGScheduler(){
        log.info("Start Auto Send to ABG Scheduler for "+Instant.now());
        List<Registration> registrationList = registrationRepository.findByBgStatus(Constants.BankGuaranteeStatus.APPROVEDBG);
        registrationList.forEach(registration -> {
            String status=null;
            Boolean hitAbg = Boolean.FALSE;
            try {
                webServicesUtil.PlnAbgApi(registration);
                hitAbg = Boolean.TRUE;
            } catch (IOException e) {
                e.printStackTrace();
            }
            Registration oldRegistration = (Registration) SerializationUtils.clone(registration);
            if (hitAbg){
                status = Constants.BankGuaranteeStatus.WAITINGBGVERIFICATION;
            }else {
                status = Constants.BankGuaranteeStatus.FAILED_BG;
            }
            registration.setBgStatus(status);
            registration.setModifiedBy("System/Ops");
            registration.setTglKirimAbg(Instant.now());
            registrationRepository.save(registration);
            savehistoryTransaction(registration.getId(), status, null);

            //TODO AUDIT TRAIL BG REGISTRATION SET STATUS WAITING VERIFICATION OR FAILED AUTO SCHEDULER
            auditTrailUtil.saveAudit( Constants.Event.UPDATE , Constants.Module.REGISTRATION, new JSONObject(oldRegistration).toString(),new JSONObject(registration).toString(), registration.getBgStatus().equalsIgnoreCase(Constants.BankGuaranteeStatus.WAITINGBGVERIFICATION) ? Constants.Remark.UPDATE_REGISTRATION_BG_WAITING_VERIFICATION_AUTO_SCHEDULER : Constants.Remark.UPDATE_REGISTRATION_BG_FAILED_AUTO_SCHEDULER,"System/Ops" );

        });
        log.info("End Auto Send to ABG Scheduler for "+Instant.now());
        return Boolean.TRUE;
    }

    @Scheduled(cron = Constants.setUpConfiguration.CRON_SCHEDULER)
    public Boolean autoVerifiedScheduler(){
        Instant dateToVerified = Instant.now().minus(Period.ofDays(29));
        log.info("Start Auto Verified Scheduler for "+dateToVerified);
        List<Registration> registrationList = registrationRepository.findByTglKirimAbgLessThanAndBgStatus(dateToVerified, Constants.BankGuaranteeStatus.WAITINGBGVERIFICATION);
        registrationList.forEach(registration -> {
            Registration oldRegistration = (Registration) SerializationUtils.clone(registration);
            registration.setBgStatus(Constants.BankGuaranteeStatus.VERIFIEDBG);
            registration.setModifiedBy("System/Ops");
            registrationRepository.save(registration);
            savehistoryTransaction(registration.getId(), Constants.BankGuaranteeStatus.VERIFIEDBG, null);

            //TODO AUDIT TRAIL BG REGISTRATION SET STATUS AUTO VERIFIED
            auditTrailUtil.saveAudit( Constants.Event.UPDATE , Constants.Module.REGISTRATION, new JSONObject(oldRegistration).toString(),new JSONObject(registration).toString(), Constants.Remark.UPDATE_REGISTRATION_BG_VERIFIED_AUTO_SCHEDULER,"System/Ops" );
        });
        log.info("End Auto Verified Scheduler for "+dateToVerified);
        return Boolean.TRUE;
    }

    @Scheduled(cron = Constants.setUpConfiguration.CRON_SCHEDULER)
    public Boolean autoSettledScheduler(){
        Date dateToSettled = dateUtil.addDays(new Date(), -29);
        log.info("Start Auto Settled Scheduler for "+dateToSettled);
        List<Registration> registrationList = registrationRepository.findByTanggalBatasClaimLessThanAndBgStatus(dateToSettled.getTime(), Constants.BankGuaranteeStatus.VERIFIEDBG);
        registrationList.forEach(registration -> {
            Registration registrationTop = registrationRepository.findTop1ByNomorJaminanAndBgStatusInOrderByNomorAmentmendDesc(registration.getNomorJaminan(), Statusutil.getStatusCheckerSettlement())
                    .orElseThrow(()->new BadRequestAlertException("Nomor Jaminan Tidak ditemukan","",""));
            if (registration.getNomorAmentmend().equalsIgnoreCase(registrationTop.getNomorAmentmend())){
                List<Registration> registrations = registrationRepository.findByNomorJaminanAndBgStatusNot(registration.getNomorJaminan(), Constants.BankGuaranteeStatus.DELETEDBG);
                registrations.forEach(registration1 -> {
                    Registration oldRegistration = (Registration) SerializationUtils.clone(registration1);
                    registration1.setBgStatus(Constants.BankGuaranteeStatus.SETTLEDBG);
                    registration1.setModifiedBy("System/Ops");
                    registrationRepository.save(registration1);
                    savehistoryTransaction(registration1.getId(), Constants.BankGuaranteeStatus.SETTLEDBG, null);

                    //TODO AUDIT TRAIL BG REGISTRATION SET STATUS AUTO VERIFIED
                    auditTrailUtil.saveAudit( Constants.Event.UPDATE , Constants.Module.REGISTRATION, new JSONObject(oldRegistration).toString(),new JSONObject(registration1).toString(), Constants.Remark.UPDATE_REGISTRATION_BG_SETTLED_AUTO_SCHEDULER,"System/Ops" );
                });
            }
        });
        log.info("End Auto Settled Scheduler for "+dateToSettled);
        return Boolean.TRUE;
    }

    @Scheduled(cron = Constants.setUpConfiguration.CRON_SCHEDULER)
    public Boolean autoExpiredScheduler(){
        Date dateToExpired = dateUtil.addDays(new Date(), -364);
        log.info("Start Auto Expired Scheduler for "+dateToExpired);
        List<Registration> registrationList = registrationRepository.findByTanggalBatasClaimLessThanAndBgStatus(dateToExpired.getTime(), Constants.BankGuaranteeStatus.VERIFIEDBG);
        registrationList.forEach(registration -> {
            Registration oldRegistration = (Registration) SerializationUtils.clone(registration);
            registration.setBgStatus(Constants.BankGuaranteeStatus.EXPIRED_BG);
            registration.setModifiedBy("System/Ops");
            registrationRepository.save(registration);
            savehistoryTransaction(registration.getId(), Constants.BankGuaranteeStatus.EXPIRED_BG, null);

            //TODO AUDIT TRAIL BG REGISTRATION SET STATUS AUTO EXPIRED
            auditTrailUtil.saveAudit( Constants.Event.UPDATE , Constants.Module.REGISTRATION, new JSONObject(oldRegistration).toString(),new JSONObject(registration).toString(), Constants.Remark.UPDATE_REGISTRATION_BG_EXPIRED_AUTO_SCHEDULER,"System/Ops" );
        });
        log.info("End Auto Expired Scheduler for "+dateToExpired);
        return Boolean.TRUE;
    }

    //TODO CHANGE TO CRON EOD IN PRODUCTION
    @Scheduled(cron = Constants.setUpConfiguration.CRON_SCHEDULER_EOD)
    public void checkUserLastLoggedInAndLastChangePassword(){
        log.info("Start User Scheduler");
        List<User> userList = userRepository.findByLastChangePasswordIsNotNullAndLastLoggedInIsNotNullAndStatusNot(Constants.UserStatus.LOCKED);
        long diffBetween = 0;
        long diffBetweenLastChangePassword=0;

        for(int i = 0; i<userList.size(); i++){
            User oldUser = (User) SerializationUtils.clone(userList.get(i));
            if(userList.get(i).getLastLoggedIn() != null){
               diffBetween =
                       dateUtil.countDays(Instant.now() , userList.get(i).getLastLoggedIn());

            }

            if(userList.get(i).getLastChangePassword() != null){
                diffBetweenLastChangePassword =
                        dateUtil.countDays(Instant.now(), userList.get(i).getLastChangePassword());
            }

            if(diffBetween > 30  && userList.get(i).getCountDisabled() < 5){
               userList.get(i).setAccountNonLocked(Boolean.FALSE);
               userList.get(i).setStatus(Constants.UserStatus.LOCKED);
               //System.out.println("Count Days : "+ diffBetween+ " || User locked : " + userList.get(i).getUsername());
                log.info("Count Days : "+ diffBetween+ " || User locked : " + userList.get(i).getUsername());
            }

            if(diffBetweenLastChangePassword > 30 && !userList.get(i).getIsForceChangePassword()){
                userList.get(i).setIsForceChangePassword(Boolean.TRUE);
                //System.out.println("Count Days : "+ diffBetween+ " || User force change password : " + userList.get(i).getUsername());
                log.info("Count Days : "+ diffBetween+ " || User force change password : " + userList.get(i).getUsername());
            }

            userRepository.save(userList.get(i));

            //TODO AUDIT TRAIL USER FORCE CHANGED PASSWORD
            auditTrailUtil.saveAudit( Constants.Event.UPDATE , Constants.Module.USER, new JSONObject(oldUser).toString(),new JSONObject(userList.get(i)).toString(), Constants.Remark.UPDATE_USER_FORCECHANGEPASS_OR_ACCOUNTLOCKED,"System/Ops" );
        }
        log.info("End User Scheduler");
    }

    private void savehistoryTransaction(Long id_jaminan, String status, String notes){
        BankGuaranteeHistoryDTO dtoHistory = new BankGuaranteeHistoryDTO();
        dtoHistory.setId(0L);
        dtoHistory.setIdJaminan(id_jaminan);
        dtoHistory.setBgStatus(status);
        dtoHistory.setNotes(notes);
        bankGuaranteeHistoryService.saveHistory(dtoHistory);
    }
}
