package com.ebizcipta.ajo.api.util;


import com.ebizcipta.ajo.api.domain.Menu;
import com.ebizcipta.ajo.api.domain.User;
import com.ebizcipta.ajo.api.exception.ResourceNotFoundException;
import com.ebizcipta.ajo.api.repositories.MenuRepository;
import com.ebizcipta.ajo.api.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class HasPrivilege {

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MessageSource messageSource;

    @Autowired
    UserRepository userRepository;

    private Boolean checkMenuAndPrivilege(String name, User user) throws ResourceNotFoundException {
        String[] split = name.split("_");
        String privilegeName = split[0];
        String privilegeAccess = split[1];

        Menu menu = menuRepository.findByNameLikeIgnoreCase(privilegeName)
                .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage("error.not.found",
                        new String[]{"Menu " + privilegeName},
                        LocaleContextHolder.getLocale())));

        if (privilegeAccess.equals(Constants.Privilege.CREATE)) {

            Long found = userRepository.getUserMenuCreatePrivilege(user.getId(), menu.getName());

            return found.intValue() > 0;
        }

        if (privilegeAccess.equals(Constants.Privilege.READ)) {
            Long found = userRepository.getUserMenuReadPrivilege(user.getId(), menu.getName());

            return found.intValue() > 0;
        }

        if (privilegeAccess.equals(Constants.Privilege.UPDATE)) {
            Long found = userRepository.getUserMenuUpdatePrivilege(user.getId(), menu.getName());

            return found.intValue() > 0;
        }

        if (privilegeAccess.equals(Constants.Privilege.DELETE)) {
            Long found = userRepository.getUserMenuDeletePrivilege(user.getId(), menu.getName());

            return found.intValue() > 0;
        }

        if (privilegeAccess.equals(Constants.Privilege.APPROVE)) {
            Long found = userRepository.getUserMenuApprovalPrivilege(user.getId(), menu.getName());

            return found.intValue() > 0;
        }

        return Boolean.FALSE;
    }

    public Boolean checkPrivilege(String privilegeName) throws ResourceNotFoundException {
        Boolean result = Boolean.FALSE;
        final Boolean[] temp = {Boolean.FALSE};

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findOneByUsernameAndIsEnabled(authentication.getName(),true)
                .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage("error.not.found",
                        new String[]{"User " + authentication.getName()},
                        LocaleContextHolder.getLocale())));

        List<String> privilegeListParam = Arrays.asList(privilegeName.split("\\s*,\\s*"));
        for (int x =0; x < privilegeListParam.size(); x++){
            try {
                result = result || checkMenuAndPrivilege(privilegeListParam.get(x), user);
            } catch (ResourceNotFoundException e) {
                log.error("ERROR ", e);
            }
        }
        return result;
    }
}
