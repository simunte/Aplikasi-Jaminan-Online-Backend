package com.ebizcipta.ajo.api.web;


import com.ebizcipta.ajo.api.service.RoleService;
import com.ebizcipta.ajo.api.service.dto.*;
import com.ebizcipta.ajo.api.util.AuditTrailUtil;
import com.ebizcipta.ajo.api.util.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Api(value="Role Management")
public class RoleManagementController {

    private final RoleService roleService;
    private final AuditTrailUtil auditTrailUtil;

    public RoleManagementController(RoleService roleService, AuditTrailUtil auditTrailUtil) {
        this.roleService = roleService;
        this.auditTrailUtil = auditTrailUtil;
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('User Access Management - User Groups_CREATE,User Access Management - User Groups_UPDATE,User Access Management - User Groups_APPROVE')")
    @PostMapping("/role")
    @ApiOperation("Add Role and Assign Privelege")
    public ResponseEntity<Boolean> addRole(@Valid @RequestBody CreateRoleDTO createRoleDTO) throws URISyntaxException{
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Role Management", "addRole");
        Boolean result = roleService.saveRoleAndAndPrivelegeMenu(createRoleDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Role Management", "addRole");
        return ResponseEntity.created(new URI("/api/v1/role"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('User Access Management - User Groups_DELETE')")
    @PostMapping("/delete-role")
    @ApiOperation("InActive role")
    public ResponseEntity<Boolean>deleteRole(@RequestBody Integer[] listId) throws URISyntaxException{
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Role Management", "deleteRole");
        Boolean result =  roleService.deleteRole(listId);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Role Management", "deleteRole");
        return ResponseEntity.created(new URI("/api/v1/delete-role"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('User Access Management - User Groups_APPROVE')")
    @PostMapping("/action")
    @ApiOperation("Approve or Reject")
    public ResponseEntity<Boolean>action(@RequestBody ApprovalRoleDTO approvalRoleDTO) throws URISyntaxException{
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Role Management", "approveRole");
        Boolean result =  roleService.approveOrRejectRole(approvalRoleDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Role Management", "approveRole");
        return ResponseEntity.created(new URI("/api/v1/action"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('User Access Management - User Groups_CREATE,User Access Management - User Groups_UPDATE,User Access Management - User Groups_APPROVE')")
    @PostMapping("/role-temp")
    @ApiOperation("Approve or Reject")
    public ResponseEntity<Boolean>roleTemp(@RequestBody RoleTempDTO roleTempDTO) throws URISyntaxException{
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Role Management", "roleTemp");
        Boolean result =  roleService.saveRoleTemp(roleTempDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Role Management", "roleTemp");
        return ResponseEntity.created(new URI("/api/v1/role-temp"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('User Access Management - User Groups_CREATE,User Access Management - User Groups_UPDATE,User Access Management - User Groups_APPROVE')")
    @PutMapping("/role")
    @ApiOperation("Update Role and Assign Privelege")
    public ResponseEntity<Boolean> updateRole(@Valid @RequestBody CreateRoleDTO createRoleDTO) throws URISyntaxException{
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Role Management", "updateRole");
        Boolean result = roleService.saveRoleAndAndPrivelegeMenu(createRoleDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Role Management", "updateRole");
        return ResponseEntity.created(new URI("/api/v1/role"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('User Access Management - User Groups_READ')")
    @GetMapping("/role")
    @ApiOperation("get All Role and Assign Privelege")
    public ResponseEntity<List<RoleListDTO>> getAllRoleAndPrivilege(@RequestParam(value = "status", required = false) String status){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Role Management", "getAllRoleAndPrivilege");
        final List<RoleListDTO> result= roleService.findAllRoleAndPrivelegeMenu(status);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Role Management", "getAllRoleAndPrivilege");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('User Access Management - User Groups_READ')")
    @GetMapping("/role-customize")
    @ApiOperation("get All Role and Assign Privelege")
    public ResponseEntity<List<RoleListDTO>> getAllRoleAndPrivilegeRealAndTemp(@RequestParam(value = "status", required = false) String status){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Role Management", "getAllRoleAndPrivilegeRealAndTemp");
        final List<RoleListDTO> result= roleService.findAllRoleAndPrivelegeMenuTempAndReal(status);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Role Management", "getAllRoleAndPrivilegeRealAndTemp");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('User Access Management - User Groups_READ,User Access Management - User Access_READ' )")
    @GetMapping("/role-user-login")
    @ApiOperation("get All Role and Assign Privelege")
    public ResponseEntity<List<RoleListDTO>> getAllRoleAndPrivilegeUserLogin(){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Role Management", "getAllRoleAndPrivilegeUserLogin");
        final List<RoleListDTO> result= roleService.findAllRoleByUserLogin();
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Role Management", "getAllRoleAndPrivilegeUserLogin");
        return ResponseEntity.ok()
                .body(result);
    }

    //TODO: need more detail
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/role/{roleId}")
    @ApiOperation("get Specific Role and Assign Privelege")
    public ResponseEntity<Optional<RoleListDTO>> getSpecificRole(@PathVariable Integer roleId){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Role Management", "getSpecificRole");
        Optional<RoleListDTO> result= roleService.findDetailRoleAndPrivelegeMenu(roleId);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Role Management", "getSpecificRole");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('User Access Management - User Groups_CREATE,User Access Management - User Groups_UPDATE,User Access Management - User Groups_APPROVE')")
    @GetMapping("/role-temp/{roleId}")
    @ApiOperation("get Role Temp")
    public ResponseEntity<RoleTempDTO> getRoleTemp(@PathVariable Integer roleId){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Role Management", "getRoleTemp");
        RoleTempDTO result= roleService.roleTemp(roleId);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Role Management", "getRoleTemp");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('User Access Management - User Groups_READ')")
    @GetMapping("/roles")
    @ApiOperation("get All Role")
    public ResponseEntity<List<RoleListDTO>> getAllRole(){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Role Management", "getAllRole");
        List<RoleListDTO> result = roleService.findAllRole();
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Role Management", "getAllRole");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('User Access Management - User Groups_READ')")
    @GetMapping("/menus")
    @ApiOperation("get All Menu")
    public ResponseEntity<List<MenuListDTO>> getAllMenu(){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "Role Management", "getAllMenu");
        List<MenuListDTO> result = roleService.findAllMenu();
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "Role Management", "getAllMenu");
        return ResponseEntity.ok()
                .body(result);
    }
}
