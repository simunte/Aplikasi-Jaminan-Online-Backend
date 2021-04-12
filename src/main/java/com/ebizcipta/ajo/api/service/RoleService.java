package com.ebizcipta.ajo.api.service;

import com.ebizcipta.ajo.api.service.dto.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Boolean saveRoleAndAndPrivelegeMenu(CreateRoleDTO createRoleDTO);
    List<RoleListDTO> findAllRoleAndPrivelegeMenu(String Status);
    List<RoleListDTO> findAllRoleAndPrivelegeMenuTempAndReal(String Status);

    List<RoleListDTO> findAllRoleByUserLogin();
    Optional<RoleListDTO> findDetailRoleAndPrivelegeMenu(Integer roleId);
    List<MenuListDTO> findAllMenu();
    List<RoleListDTO> findAllRole();
    Boolean deleteRole(Integer[] listId);
    RoleTempDTO roleTemp(Integer id);

    Boolean approveOrRejectRole(ApprovalRoleDTO approvalRoleDTO);
    Boolean saveRoleTemp(RoleTempDTO roleTempDTO);
}
