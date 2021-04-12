package com.ebizcipta.ajo.api.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;


@Entity
@Data
@Table(name = "audit_log")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "date_time")
    private Instant dateTime;
    private String username;
    @Column(name = "ip_address", length = 15)
    private String ipAddress;
    @Column(name = "module", length = 30)
    private String module;
    @Column(name = "event", length = 10)
    private String event;
    @Column(name = "remark", length = 100, updatable = false)
    private String remark;
    @Lob
    @Column(name = "payload_before")
    private String payloadBefore;
    @Lob
    @Column(name = "payload_after")
    private String payloadAfter;

    @PrePersist
    public void prePersist() {
        this.dateTime = Instant.now();
    }
}
