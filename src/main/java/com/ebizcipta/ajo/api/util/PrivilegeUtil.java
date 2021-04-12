package com.ebizcipta.ajo.api.util;

import com.ebizcipta.ajo.api.domain.Menu;
import com.ebizcipta.ajo.api.domain.Privilege;
import com.ebizcipta.ajo.api.exception.ResourceNotFoundException;
import com.ebizcipta.ajo.api.repositories.MenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class PrivilegeUtil {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    MenuRepository menuRepository;

    public Privilege getPrivilege(String menu, String privilege) throws ResourceNotFoundException {


        Privilege p = new Privilege();
        p.setCreate(Boolean.FALSE);
        p.setRead(Boolean.FALSE);
        p.setUpdate(Boolean.FALSE);
        p.setDelete(Boolean.FALSE);
        p.setApproval(Boolean.FALSE);

        // get menu
        Menu m = menuRepository.findByNameLikeIgnoreCase(menu)
                .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage("error.not.found",
                        new String[]{"Menu " + menu},
                        LocaleContextHolder.getLocale())));
        p.setMenu(m);

        // check all privilege
        if (privilege.equals(Constants.Privilege.ALL)) {
            p.setCreate(Boolean.TRUE);
            p.setRead(Boolean.TRUE);
            p.setUpdate(Boolean.TRUE);
            p.setDelete(Boolean.TRUE);
            p.setApproval(Boolean.TRUE);

            return p;
        }

        // check privilege
        String[] privileges = privilege.split("_");
        for (String x: privileges) {
            switch (x) {
                case Constants.Privilege.CREATE:
                    p.setCreate(Boolean.TRUE);
                    break;
                case Constants.Privilege.READ:
                    p.setRead(Boolean.TRUE);
                    break;
                case Constants.Privilege.UPDATE:
                    p.setUpdate(Boolean.TRUE);
                    break;
                case Constants.Privilege.DELETE:
                    p.setDelete(Boolean.TRUE);
                    break;
                default:
                    p.setApproval(Boolean.TRUE);
            }
        }

        return p;
    }

    public Boolean comparePrivilege(Privilege existPriv, Privilege setPriv){
        Boolean result = Boolean.FALSE;
        if (setPriv.getCreate() && existPriv.getCreate()){
            result = (Boolean.TRUE);
        }
        if (setPriv.getRead() && existPriv.getRead()){
            result = (Boolean.TRUE);
        }
        if (setPriv.getUpdate() && existPriv.getUpdate()){
            result = (Boolean.TRUE);
        }
        if (setPriv.getDelete() && existPriv.getDelete()){
            result = (Boolean.TRUE);
        }
        if (setPriv.getApproval() && existPriv.getApproval()){
            result = (Boolean.TRUE);
        }
        return result;
    }

}
