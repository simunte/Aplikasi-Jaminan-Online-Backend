package com.ebizcipta.ajo.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "master_configuration")
public class MasterConfiguration extends Base{
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "smtp_hostname")
    private String smtpHostname;

    @Column(name = "smtp_password")
    private String smtpPassword;

    @Column(name = "smtp_port")
    private String smtpPort;

    @Column(name = "retention_data")
    private String retentionData;

    @Column(name = "mailing_list")
    private String mailingList;

    private Boolean activated;

    private String notes;

    private String status;
}
