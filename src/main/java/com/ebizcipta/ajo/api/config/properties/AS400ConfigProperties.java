package com.ebizcipta.ajo.api.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "as400")
public class AS400ConfigProperties {
    private String driver;
    private String url;
    private String username;
    private String password;
    private String file;
}
