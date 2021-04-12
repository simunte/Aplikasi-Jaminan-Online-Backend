package com.ebizcipta.ajo.api.service.impl;

import com.ebizcipta.ajo.api.domain.AuditLog;
import com.ebizcipta.ajo.api.repositories.AuditLogRepository;
import com.ebizcipta.ajo.api.service.AuditLogService;
import com.ebizcipta.ajo.api.service.dto.AuditLogDTO;
import com.ebizcipta.ajo.api.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class AuditLogServiceImpl implements AuditLogService {

    @Autowired
    AuditLogRepository auditLogRepository;
    @Autowired
    private DateUtil dateUtil;

    @Override
    public void saveAuditLog(AuditLog auditLog) {
        auditLogRepository.save(auditLog);
    }

    @Override
    public List<AuditLogDTO> getListAuditLog(Long startDate, Long endDate, String module) {
        List<AuditLog> auditLogList = null;
        if (module.trim().toLowerCase().equalsIgnoreCase("all")){
            auditLogList = auditLogRepository.findByDateTimeBetweenAndUsernameNotOrderByDateTime(dateUtil.longToInstant(startDate), dateUtil.longToInstant(endDate), "System/Ops");
        }else{
            auditLogList = auditLogRepository.findByDateTimeBetweenAndModuleAndUsernameNotOrderByDateTime(dateUtil.longToInstant(startDate), dateUtil.longToInstant(endDate), module, "System/Ops");
        }
        return auditLogList.stream()
                .map(auditLog -> {
                    AuditLogDTO auditLogDTO = new AuditLogDTO();
                    auditLogDTO.setId(auditLog.getId());
                    auditLogDTO.setDateTime(auditLog.getDateTime().toEpochMilli());
                    auditLogDTO.setUsername(auditLog.getUsername());
                    auditLogDTO.setIpAddress(auditLog.getIpAddress());
                    auditLogDTO.setModule(auditLog.getModule());
                    auditLogDTO.setEvent(auditLog.getEvent());
                    auditLogDTO.setRemark(auditLog.getRemark());
                    auditLogDTO.setPayloadBefore(auditLog.getPayloadBefore());
                    auditLogDTO.setPayloadAfter(auditLog.getPayloadAfter());
                    return auditLogDTO;
                })
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
