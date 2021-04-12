package com.ebizcipta.ajo.api.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "security")
public class SecurityConfigProperties {
    private String securityRealm;
    private String keyStorePassword;
    private JWT jwt;

    @Setter
    @Getter
    public static class JWT {
        private String clientId;
        private String clientSecret;
        private String grantType;
        private String scopeRead;
        private String scopeWrite;
        private String resourceIds;
        private Integer accessTokenValidity;
        private Integer refreshTokenValidity;
    }
}
