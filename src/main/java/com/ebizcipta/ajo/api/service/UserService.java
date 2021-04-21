package com.ebizcipta.ajo.api.service;

import com.ebizcipta.ajo.api.service.dto.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Boolean saveUser(UserCreateDTO userCreateDTO);
    Boolean saveUserNasabah(UserNasabahDTO userNasabahDTO);
    Optional <UserNasabahDTO> loadUserNasabahByUsername(String username);
    Boolean deleteUser(Integer[] userId);


    Boolean changePasswordUserExternal(ChangePasswordDTO changePasswordDTO);
    Optional <UserListDTO> findUserByUsername(String username);

    List<UserListDTO> listOutstandingUser();
    List <UserListDTO> findAllUser(String status);
    Boolean userApproval(Long userId, String approvalType, Boolean approve);
    Boolean isFirstLogin();
    Boolean approveOrRejectUser(ApprovalUserDTO approvalUserDTO);
    Boolean countDisabled(String username);
    Boolean unlockUser(String username);
    Boolean resetPassword(String username);
    Boolean resetCountDisabled(String username);
    List<UserHistoryDTO> userHistory(Long id);
    void createLogoutAuditTrail();
}
