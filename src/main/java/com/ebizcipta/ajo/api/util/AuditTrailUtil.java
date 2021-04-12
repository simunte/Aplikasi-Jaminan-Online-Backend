package com.ebizcipta.ajo.api.util;

import com.ebizcipta.ajo.api.domain.AuditLog;
import com.ebizcipta.ajo.api.service.AuditLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@Component
public class AuditTrailUtil {

    @Autowired
    AuditLogService auditLogService;

    public void saveAudit(String eventAudit , String module, String payloadBefore, String payloadAfter, String remark, String username) {

        AuditLog auditLog = new AuditLog();

        if(username == null || username.length()<=0){
            username = "System/Ops";
        }

        auditLog.setUsername(username);
        auditLog.setDateTime(Instant.now());
        auditLog.setEvent(eventAudit);
        auditLog.setModule(module);
        auditLog.setPayloadBefore(payloadBefore);
        auditLog.setPayloadAfter(payloadAfter);
        auditLog.setRemark(remark);
        auditLogService.saveAuditLog(auditLog);
    }

    public void printLogDetailStartTime(String kindOfExecution, String moduleName, String methodName){
        Instant nowUtc = Instant.now();
        ZoneId asiaSingapore = ZoneId.systemDefault();
        ZonedDateTime dateNow = ZonedDateTime.ofInstant(nowUtc, asiaSingapore);

        if (kindOfExecution.equalsIgnoreCase(Constants.ExecutionType.START_EXECUTION)){
            System.out.println("------- "+kindOfExecution+" EXECUTION METHOD DETAIL TIME -------");
            System.out.println("MODULE : "+ moduleName);
            System.out.println("METHOD : "+ methodName);
            System.out.println("START EXECUTION TIME : "+ dateNow);
        }else if (kindOfExecution.equalsIgnoreCase(Constants.ExecutionType.END_EXECUTION)){
            System.out.println("END EXECUTION TIME : "+ dateNow);
            System.out.println("-----------------END--------------------");
        }else {
            log.info("UNKNOWN PROCESS EXECUTION");
        }
    }
}
