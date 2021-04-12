package com.ebizcipta.ajo.api.config;

import com.ebizcipta.ajo.api.util.Constants;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
@EnableJpaAuditing
public class AuditorAwareConfig implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            return Optional.of(SecurityContextHolder.getContext().getAuthentication().getName());
        } catch (Exception e){
            return Optional.of(Constants.User.SYSTEM_ACCOUNT);
        }
    }
}
