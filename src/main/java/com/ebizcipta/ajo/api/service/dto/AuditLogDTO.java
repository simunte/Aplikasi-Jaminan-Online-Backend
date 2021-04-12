package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Lob;

@Data
public class AuditLogDTO {
    private Long id;

    @JsonProperty(value = "date_time")
    private Long dateTime;

    private String username;

    @JsonProperty(value = "ip_address")
    private String ipAddress;

    @JsonProperty(value = "module")
    private String module;

    @JsonProperty(value = "event")
    private String event;

    @JsonProperty(value = "remark")
    private String remark;

    @Lob
    @JsonProperty(value = "payload_before")
    private String payloadBefore;

    @Lob
    @JsonProperty(value = "payload_after")
    private String payloadAfter;
}
