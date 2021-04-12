package com.ebizcipta.ajo.api.web;


import com.ebizcipta.ajo.api.service.UserService;
import com.ebizcipta.ajo.api.service.dto.*;
import com.ebizcipta.ajo.api.util.AuditTrailUtil;
import com.ebizcipta.ajo.api.util.Constants;
import com.ebizcipta.ajo.api.util.EmailUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Api(value="User Management")
public class UserManagementController {

    @Autowired
    TokenStore tokenStore;

    private final UserService userService;
    private final EmailUtil emailUtil;
    private final AuditTrailUtil auditTrailUtil;

    public UserManagementController(UserService userService, EmailUtil emailUtil, AuditTrailUtil auditTrailUtil) {
        this.userService = userService;
        this.emailUtil = emailUtil;
        this.auditTrailUtil = auditTrailUtil;
    }

    /* START USER API */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> logout(HttpServletRequest request) {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "User Management", "logout");
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            //TODO AUDIT TRAIL LOGOUT
            userService.createLogoutAuditTrail();
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
        }
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "User Management", "logout");
        return ResponseEntity.ok().body(Boolean.TRUE);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('User Access Management - User Access_READ')")
    @GetMapping("/users")
    @ApiOperation("get All Users")
    public ResponseEntity<List<UserListDTO>> getAllUsers(@RequestParam(value = "status", required = false) String status) {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "User Management", "getAllUsers");
        final List<UserListDTO> result = userService.findAllUser(status);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "User Management", "getAllUsers");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('User Access Management - User Access_READ')")
    @GetMapping("/list-user-outstanding")
    @ApiOperation("get All User Outstanding")
    public ResponseEntity<List<UserListDTO>> listUserOutstanding() {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "User Management", "listUserOutstanding");
        final List<UserListDTO> result = userService.listOutstandingUser();
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "User Management", "listUserOutstanding");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Home_READ')")
    @GetMapping("/users/{username}")
    @ApiOperation("get Users By Username")
    public ResponseEntity<Optional<UserListDTO>> getUsers(@PathVariable String username) {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "User Management", "getUsers");
        Optional<UserListDTO> result = userService.findUserByUsername(username);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "User Management", "getUsers");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('User Access Management - User Access_CREATE,User Access Management - User Access_UPDATE')")
    @PostMapping("/users")
    @ApiOperation("Add User")
    public ResponseEntity<Boolean> addUser(@Valid @RequestBody UserCreateDTO userCreateDTO) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "User Management", "addUser");
        Boolean result = userService.saveUser(userCreateDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "User Management", "addUser");
        return ResponseEntity.created(new URI("/api/v1/users"+userCreateDTO.getUsername()))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Home_READ')")
    @PostMapping("/change-password")
    @ApiOperation("Change Password")
    public ResponseEntity<Boolean> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "User Management", "changePassword");
        Boolean result = userService.changePasswordUserExternal(changePasswordDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "User Management", "changePassword");
        return ResponseEntity.created(new URI("/api/v1/change-password"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('Home_READ')")
    @PostMapping("/first-login")
    @ApiOperation("First Login")
    public ResponseEntity<Boolean> isFirstLogin() throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "User Management", "isFirstLogin");
        Boolean result = userService.isFirstLogin();
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "User Management", "isFirstLogin");
        return ResponseEntity.created(new URI("/api/v1/first-login"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('User Access Management - User Access_APPROVE')")
    @PostMapping("/users/approve-reject")
    @ApiOperation("Approve or Reject")
    public ResponseEntity<Boolean> approveReject(@RequestBody ApprovalUserDTO approvalUserDTO) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "User Management", "approveReject");
        Boolean result = userService.approveOrRejectUser(approvalUserDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "User Management", "approveReject");
        return ResponseEntity.created(new URI("/api/v1/users/approve-reject"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('User Access Management - User Access_READ')")
    @PostMapping("/unlock")
    @ApiOperation("Unlock user")
    public ResponseEntity<Boolean> unlock(@RequestBody String username) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "User Management", "unlock");
        Boolean result = userService.unlockUser(username);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "User Management", "unlock");
        return ResponseEntity.created(new URI("/api/v1/unlock"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('User Access Management - User Access_READ')")
    @PostMapping("/reset-password")
    @ApiOperation("reset-password")
    public ResponseEntity<Boolean> resetPassword(@RequestBody String username) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "User Management", "resetPassword");
        Boolean result = userService.resetPassword(username);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "User Management", "resetPassword");
        return ResponseEntity.created(new URI("/api/v1/reset-password"))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('User Access Management - User Access_CREATE,User Access Management - User Access_UPDATE')")
    @PutMapping("/users")
    @ApiOperation("Update User")
    public ResponseEntity<Boolean> updateUser(@Valid @RequestBody UserCreateDTO userCreateDTO) throws URISyntaxException {
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "User Management", "updateUser");
        Boolean result = userService.saveUser(userCreateDTO);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "User Management", "updateUser");
        return ResponseEntity.created(new URI("/api/v1/users"+userCreateDTO.getUsername()))
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('User Access Management - User Access_DELETE')")
    @PostMapping("/delete-user")
    @ApiOperation("Delete User")
    public ResponseEntity<Boolean> reqDelete(@RequestBody Integer[] userId){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "User Management", "reqDelete");
        Boolean result = userService.deleteUser(userId);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "User Management", "reqDelete");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('User Access Management - User Access_APPROVE')")
    @GetMapping("/users/approval/{userId}")
    @ApiOperation("Approval for New and Delete User")
    public ResponseEntity<Boolean> approvalUser(@PathVariable Long userId,
                                                @RequestParam(value = "approvalType", defaultValue = "false") String approvalType,
                                                @RequestParam(value = "approve", defaultValue = "false") Boolean approve){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "User Management", "approvalUser");
        Boolean result = userService.userApproval(userId, approvalType, approve);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "User Management", "approvalUser");
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@hasPrivilege.checkPrivilege('User Access Management - User Access_READ')")
    @GetMapping("/user-history/{userId}")
    @ApiOperation("User History")
    public ResponseEntity<List<UserHistoryDTO>> userHistoryDTOResponseEntity(@PathVariable Long userId){
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.START_EXECUTION, "User Management", "userHistoryDTOResponseEntity");
        final List<UserHistoryDTO> result = userService.userHistory(userId);
        auditTrailUtil.printLogDetailStartTime(Constants.ExecutionType.END_EXECUTION, "User Management", "userHistoryDTOResponseEntity");
        return ResponseEntity.ok().body(result);
    }
}
