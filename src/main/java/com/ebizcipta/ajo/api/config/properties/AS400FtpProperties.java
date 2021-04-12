package com.ebizcipta.ajo.api.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "ftpas400")
public class AS400FtpProperties {
    private String server;
    private String port;
    private String username;
    private String password;
}
