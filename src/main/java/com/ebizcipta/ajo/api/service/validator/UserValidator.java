package com.ebizcipta.ajo.api.service.validator;

import com.ebizcipta.ajo.api.domain.Role;
import com.ebizcipta.ajo.api.domain.User;
import com.ebizcipta.ajo.api.exception.BadRequestAlertException;
import com.ebizcipta.ajo.api.exception.GlobalExceptionHandler;
import com.ebizcipta.ajo.api.exception.ResourceNotFoundException;
import com.ebizcipta.ajo.api.repositories.RoleRepository;
import com.ebizcipta.ajo.api.repositories.UserRepository;
import com.ebizcipta.ajo.api.service.dto.RoleDTO;
import com.ebizcipta.ajo.api.service.dto.UserCreateDTO;
import com.ebizcipta.ajo.api.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserValidator {

    @Autowired
    RoleRepository roleRepository;

    public List<Role> checkRoles(List<RoleDTO> roleDTOS){
        List<Role> roleList = roleDTOS == null ? null : roleDTOS.stream()
                .map(role -> {
                    Role roleFound = roleRepository.findById(role.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Role Not Found"));
                    return roleFound;
                }).collect(Collectors.toCollection(LinkedList::new));
        return roleList;
    }

    public String checkReqUser(UserCreateDTO userCreateDTO){
        String errorMsg = null;
        if(userCreateDTO.getFirstName() == null || userCreateDTO.getFirstName().isEmpty()){
            errorMsg = "Employee name wajib disi";
        }else if(userCreateDTO.getUsername() == null || userCreateDTO.getUsername().isEmpty()){
            errorMsg = "Username wajib disi";
        }else if(userCreateDTO.getRoles() == null || userCreateDTO.getRoles().isEmpty()) {
            errorMsg = "Role wajib disi";
        }else if(userCreateDTO.getRoles().get(0).getCode().equalsIgnoreCase(Constants.Role.NASABAH) && userCreateDTO.getEmail() == null){
            errorMsg = "Email tidak boleh kosong";
        }
        else if(userCreateDTO.getEmail() != null && !userCreateDTO.getEmail().trim().isEmpty() && !this.validateEmailFormat(userCreateDTO.getEmail())){
            errorMsg = "Format email tidak benar";
        }else if(userCreateDTO.getRoles().isEmpty()){
            errorMsg = "Role tidak boleh kosong";
        }
        return errorMsg;
    }

    public Boolean validateEmailFormat(String email){
            String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
            return email.matches(regex);
    }

    public List<String> approvalValidator(User user, String approvalType, Boolean approve){
        List <String> result = new ArrayList<>();
        Boolean bool = Boolean.TRUE;
        if (approvalType.toUpperCase().equalsIgnoreCase(Constants.User.APPROVAL_TYPE_ACTIVATED)){
            if (!user.getStatus().toUpperCase().equalsIgnoreCase(Constants.UserStatus.CREATE_NEW))
                throw new BadRequestAlertException("User status must be : " +Constants.UserStatus.CREATE_NEW, "", "");
            else {
                if (approve){
                    result.add(bool.toString());
                    result.add(Constants.UserStatus.APPROVE_NEW);
                    result.add(Constants.Event.ACTIVATED);
                    result.add(Constants.Remark.CREATE_USER_STATUS_APPROVED);
                }else{
                    bool = Boolean.FALSE;
                    result.add(bool.toString());
                    result.add(Constants.UserStatus.REJECT_NEW);
                    result.add(Constants.Event.ACTIVATED);
                    result.add(Constants.Remark.CREATE_USER_STATUS_REJECTED);
                }
            }
        }else if (approvalType.toUpperCase().equalsIgnoreCase(Constants.User.APPROVAL_TYPE_DELETED)) {
            if (!user.getStatus().toUpperCase().equalsIgnoreCase(Constants.UserStatus.REQ_DELETED))
                throw new BadRequestAlertException("User status must be "+Constants.UserStatus.REQ_DELETED, "", "");
            else {
                if (approve){
                    bool = Boolean.FALSE;
                    result.add(bool.toString());
                    result.add(Constants.UserStatus.REJECT_NEW);
                    result.add(Constants.Event.DEACTIVATED);
                    result.add(Constants.Remark.DELETE_USER_STATUS_APPROVED);
                }else {
                    result.add(bool.toString());
                    result.add(Constants.UserStatus.REJECT_NEW);
                    result.add(Constants.Event.DEACTIVATED);
                    result.add(Constants.Remark.DELETE_USER_STATUS_REJECTED);
                }
            }
        }else {
            throw new BadRequestAlertException("Unknown Approval Type", "", "");
        }
        return result;
    }
}
