package com.ebizcipta.ajo.api.service.impl;

import com.ebizcipta.ajo.api.domain.*;
import com.ebizcipta.ajo.api.exception.AjoException;
import com.ebizcipta.ajo.api.exception.BadRequestAlertException;
import com.ebizcipta.ajo.api.repositories.*;
import com.ebizcipta.ajo.api.service.UserService;
import com.ebizcipta.ajo.api.service.dto.*;
import com.ebizcipta.ajo.api.service.mapper.UserHistoryMapper;
import com.ebizcipta.ajo.api.service.mapper.UserMapper;
import com.ebizcipta.ajo.api.service.validator.UserValidator;
import com.ebizcipta.ajo.api.util.AuditTrailUtil;
import com.ebizcipta.ajo.api.util.Constants;
import com.ebizcipta.ajo.api.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService{
    private static final String ERROR_NOT_FOUND = "error.not.found";
    private final UserRepository userRepository;
    private final MessageSource messageSource;
    private final ColumnUserServiceImpl columnUserService;
    private final PasswordHistoryRepository passwordHistoryRepository;

    @Autowired
    private UserValidator userValidator;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BeneficiaryRepository beneficiaryRepository;
    @Autowired
    private MasterConfigurationRepository masterConfigurationRepository;
    @Autowired
    private UserUtil userUtil;
    @Autowired
    private UserHistoryRepository userHistoryRepository;
    @Autowired
    private AuditTrailUtil auditTrailUtil;

    public UserServiceImpl(UserRepository userRepository, MessageSource messageSource, ColumnUserServiceImpl columnUserService, PasswordHistoryRepository passwordHistoryRepository) {
        this.userRepository = userRepository;
        this.messageSource = messageSource;
        this.columnUserService = columnUserService;
        this.passwordHistoryRepository = passwordHistoryRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findTop1ByUsernameAndApprovedDateIsNotNullOrderByCreationDateDesc(s)
                .orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage(ERROR_NOT_FOUND,
                        new String[]{s},
                        LocaleContextHolder.getLocale())));

        //System.out.println();
        if(user.getStatus().equals(Constants.UserStatus.LOCKED)){
            throw new AjoException("Account Locked");
        }else if(user.getStatus().equals(Constants.UserStatus.DELETED)){
            throw new AjoException("Username Inactive");
        }
        user.setLastLoggedIn(Instant.now());
        userRepository.save(user);

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                user.getAuthorities());
    }


    @Transactional
    @Override
    public Boolean saveUser(UserCreateDTO userCreateDTO) {
        log.info("REQUEST:{}" ,userCreateDTO);

        String error = userValidator.checkReqUser(userCreateDTO);
        if(error != null){
            throw new AjoException(error);
        }

        if(userCreateDTO.getEmail() != null && !userCreateDTO.getEmail().trim().isEmpty()){
            Boolean validateEamil = userValidator.validateEmailFormat(userCreateDTO.getEmail());
            if(!validateEamil){
                throw new AjoException("Format email tidak benar");
            }
        }

        if(!userCreateDTO.getRoles().get(0).getStatus().equals(Constants.RoleStatus.ACTIVE)){
            throw new AjoException("Silahkan hubungi BANK ADMIN 1 , user group ini sedang di modifikasi");
        }

        if(userCreateDTO.getRoles().isEmpty()){
            throw new AjoException("Role tidak boleh kosong");
        }

        if(userCreateDTO.getRoles().get(0).getName().equals(Constants.Role.TRO_CHECKER)){
            if(userCreateDTO.getPosition() == null || userCreateDTO.getSignature() == null
                    || userCreateDTO.getPosition().isEmpty() || userCreateDTO.getSignature().isEmpty()){
                throw new AjoException("Position / Signature tidak boleh kosong");
            }else if( userCreateDTO.getEmail() == null || userCreateDTO.getEmail().isEmpty()){
                throw new AjoException("Email tidak boleh kosong");
            }
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();
        Optional<User> users = userRepository.findOneByUsernameAndIsEnabled(userLogin,true);
        User userCreator = users.get();
        String validateRole = userCreator.getRoles().get(0).getCode();
        Beneficiary beneficiary1 = null;
        if(userCreateDTO.getBeneficiaryId() != null){
            Optional<Beneficiary> beneficiary = beneficiaryRepository.findById(userCreateDTO.getBeneficiaryId());
             beneficiary1 = beneficiary.get();
        }

        List<Role> roleList = userValidator.checkRoles(userCreateDTO.getRoles());
        if(!validateRole.equals(roleList.get(0).getRoleCreate()) || roleList.isEmpty()){
            throw new AjoException("Role Anda tidak memiliki autorisasi");
        }

        User user = new User();
        if (userCreateDTO.getId() == null){
            Optional<MasterConfiguration> masterConfiguration = masterConfigurationRepository.findTop1ByStatusOrderByCreationDateDesc("ACTIVE");
            if(!masterConfiguration.isPresent()){
                throw new AjoException("Default password belum tersedia ,  silahkan hubungi Super Admin");
            }
            String encryptedPassword = new BCryptPasswordEncoder().encode(masterConfiguration.get().getPassword());
            user.setPassword(encryptedPassword);
            user.setEnabled(true);
            user.setStatus(Constants.UserStatus.CREATE_NEW);
            user.setSignature(userCreateDTO.getSignature());

            //user external
            if(validateRole.equals(Constants.Role.BENEFICIARY_ADMIN_1)){
                user.setIsExternal(Boolean.TRUE);
                user.setBeneficiary(userCreator.getBeneficiary());
                user.setPassword(encryptedPassword);
            }else if(validateRole.equals(Constants.Role.BANK_ADMIN_2_MAKER)){
                if(userCreateDTO.getBeneficiaryId() == null) throw new AjoException("Beneficiary wajib disi");
                user.setBeneficiary(beneficiary1);
                user.setPassword(encryptedPassword);
                user.setIsExternal(Boolean.TRUE);
            }

            if(validateRole.equals(Constants.Role.BANK_ADMIN_1_MAKER ) || validateRole.equals(Constants.Role.IT)){
                user.setNeedApprovalOrReject(Constants.Role.BANK_ADMIN_1_CHECKER);
            }else if(validateRole.equals(Constants.Role.BANK_ADMIN_2_MAKER)){
                user.setNeedApprovalOrReject(Constants.Role.BANK_ADMIN_2_CHECKER);
            }else if(validateRole.equals(Constants.Role.BENEFICIARY_ADMIN_1)){
                user.setNeedApprovalOrReject(Constants.Role.BENEFICIARY_ADMIN_2);
            }

            user.setRoles(roleList != null ? roleList : null);
            user.setIsFirstLogin(Boolean.TRUE);
            user.setUpdateManualBy(userLogin);
            user.setEmail(userCreateDTO.getEmail());
            user.setUsername(userCreateDTO.getUsername());
            user.setPosition(userCreateDTO.getPosition());
            user.setFirstName(userCreateDTO.getFirstName());

            Optional<User> checkUsername = userRepository.findOneByUsernameAndIsEnabled(userCreateDTO.getUsername() , true);
            if(checkUsername.isPresent()){
                throw new AjoException("Username sudah terdaftar");
            }
            User userSaved = userRepository.save(user);

            PasswordHistory passwordHistory = new PasswordHistory();
            passwordHistory.setPassword(encryptedPassword);
            passwordHistory.setUser(userSaved);
            passwordHistoryRepository.save(passwordHistory);
            userUtil.userHistory(userSaved, userLogin,Constants.action.CREATED_USER_BY_MAKER, null);

            //TODO AUDIT TRAIL
            auditTrailUtil.saveAudit(
                    Constants.Event.CREATE,
                    Constants.Module.USER,
                    null,
                    new JSONObject(user).toString(),
                    Constants.Remark.CREATE_USER_WAITING_APPROVAL,
                    authentication.getName()
            );

        }else {
            User updateUser = userRepository.findById(userCreateDTO.getId())
                    .orElseThrow(() -> new EntityNotFoundException("User Not Found"));
            User oldUser = (User) SerializationUtils.clone(updateUser);
            Optional<User> checkUsername = userRepository.findOneByUsernameAndIsEnabled(userCreateDTO.getUsername() , true);
            if (checkUsername.isPresent()) {
                if (checkUsername.get().getId() != updateUser.getId()) {
                    throw new AjoException("Username sudah terdaftar");
                }
            }
            updateUser.setUsername(userCreateDTO.getUsername());
            updateUser.setFirstName(userCreateDTO.getFirstName());
            updateUser.setEmail(userCreateDTO.getEmail());
            updateUser.setEnabled(true);
            updateUser.setStatus(Constants.UserStatus.CREATE_NEW);
            updateUser.setSignature(userCreateDTO.getSignature());
            updateUser.setRoles(roleList != null ? roleList : null);
            updateUser.setPosition(userCreateDTO.getPosition());
            updateUser.setUpdateManualBy(userLogin);

            //user external
            if(validateRole.equals(Constants.Role.BENEFICIARY_ADMIN_1)){
                updateUser.setIsExternal(Boolean.TRUE);
                updateUser.setBeneficiary(userCreator.getBeneficiary());
            }else if(validateRole.equals(Constants.Role.BANK_ADMIN_2_MAKER)){
                updateUser.setBeneficiary(beneficiary1);
                updateUser.setIsExternal(Boolean.TRUE);
            }

            if(validateRole.equals(Constants.Role.BANK_ADMIN_1_MAKER)){
                updateUser.setNeedApprovalOrReject(Constants.Role.BANK_ADMIN_1_CHECKER);
            }else if(validateRole.equals(Constants.Role.BANK_ADMIN_2_MAKER)){
                updateUser.setNeedApprovalOrReject(Constants.Role.BANK_ADMIN_2_CHECKER);
            }else if(validateRole.equals(Constants.Role.BENEFICIARY_ADMIN_1)){
                updateUser.setNeedApprovalOrReject(Constants.Role.BENEFICIARY_ADMIN_2);
            }

            User userSaved =  userRepository.save(updateUser);
            userUtil.userHistory(userSaved, userLogin,Constants.action.CREATED_USER_BY_MAKER, null);

            //TODO AUDIT TRAIL
            auditTrailUtil.saveAudit(
                    Constants.Event.UPDATE,
                    Constants.Module.USER,
                    new JSONObject(oldUser).toString(),
                    new JSONObject(updateUser).toString(),
                    Constants.Remark.UPDATE_USER_WAITING_APPROVAL,
                    authentication.getName()
            );
        }

        return Boolean.TRUE;
    }

    @Transactional
    @Override
    public Boolean deleteUser(Integer[] userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();
        for(int i=0; i<userId.length; i++) {
            Optional<User> user = userRepository.findById(userId[i].longValue());
            User oldUser = (User) SerializationUtils.clone(user.get());
            User getUser = user.get();
            if(!getUser.getStatus().equals(Constants.UserStatus.APPROVE_NEW)){
                throw new AjoException("Status User Harus Active");
            }
            getUser.setStatus(Constants.UserStatus.REQ_DELETED);
            getUser.setEnabled(Boolean.TRUE);
            getUser.setUpdateManualBy(userLogin);
            userRepository.save(getUser);
            userUtil.userHistory(getUser, userLogin,Constants.action.DELETED_USER_BY_MAKER, null);

            //TODO AUDIT TRAIL
            auditTrailUtil.saveAudit(
                    Constants.Event.DELETE,
                    Constants.Module.USER,
                    new JSONObject(oldUser).toString(),
                    new JSONObject(user).toString(),
                    Constants.Remark.DELETE_USER_WAITING_APPROVAL,
                    userLogin
            );
        }
        return Boolean.TRUE;
    }


    @Transactional
    @Override
    public Boolean isFirstLogin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();
        Optional<User> users = userRepository.findOneByUsernameAndIsEnabled(userLogin,true);
        User userLoggedin = users.get();
        User oldUser = (User) SerializationUtils.clone(userLoggedin);
        userLoggedin.setIsFirstLogin(Boolean.FALSE);
        userRepository.save(userLoggedin);

        //TODO CREATE AUDIT TRAIL
        auditTrailUtil.saveAudit(
                Constants.Event.UPDATE,
                Constants.Module.USER,
                new JSONObject(oldUser).toString(),
                new JSONObject(userLoggedin).toString(),
                Constants.Remark.USER_FIRST_LOGIN,
                userLogin
        );
        return Boolean.TRUE;
    }


    @Transactional
    @Override
    public Boolean changePasswordUserExternal(ChangePasswordDTO changePasswordDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findById(changePasswordDTO.getUserId());
        User users = user.get();
        User oldUser = (User) SerializationUtils.clone(users);
        Pattern pattern = Pattern.compile(Constants.User.USERNAME_REGEX);
        Matcher matcher = pattern.matcher(changePasswordDTO.getNewPassword());
        if(!user.isPresent()){
            throw new AjoException("User tidak ditemukan");
        } else if(!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getReType())){
            throw new AjoException("Password tidak sama");
        }else if(!matcher.matches())
        {
            throw new AjoException("Password terdiri minimal 8 angka, terdiri dari special character dan alphanumeric");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(changePasswordDTO.getNewPassword());
        List<PasswordHistory> passwordHistoryList = passwordHistoryRepository.findByUser(users);
        Boolean lastIdx = Boolean.FALSE;
        PasswordHistory passwordHistory = null;
        PasswordHistory deleteLastIndex = null;
        if(passwordHistoryList.size() < 5){
            passwordHistory = new PasswordHistory();
            passwordHistory.setPassword(encryptedPassword);
            passwordHistory.setUser(users);
        }else if(passwordHistoryList.size() == 5){
            //delete first index
            lastIdx=Boolean.TRUE;
            deleteLastIndex = passwordHistoryList.get(0);

            //saved new password
            passwordHistory = new PasswordHistory();
            passwordHistory.setPassword(encryptedPassword);
            passwordHistory.setUser(users);

        }

        for(int i = 0; i<passwordHistoryList.size(); i++){
            boolean matchPassword =
                    new BCryptPasswordEncoder().matches
                            (changePasswordDTO.getNewPassword() , passwordHistoryList.get(i).getPassword());
            if(matchPassword){
                throw new AjoException("Password lama tidak boleh digunakan");
            }
        }

        if(lastIdx){
            passwordHistoryRepository.delete(deleteLastIndex);
        }

        passwordHistoryRepository.save(passwordHistory);
        users.setPassword(encryptedPassword);
        users.setLastChangePassword(Instant.now());
        users.setIsForceChangePassword(Boolean.FALSE);
        userRepository.save(users);

        //TODO AUDIT TRAIL
        auditTrailUtil.saveAudit(
                Constants.Event.UPDATE,
                Constants.Module.USER,
                new JSONObject(oldUser).toString(),
                new JSONObject(users).toString(),
                Constants.Remark.USER_CHANGED_PASSWORD,
                authentication.getName()
        );
        return Boolean.TRUE;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserListDTO> findUserByUsername(String username) {
        return userRepository.findOneByUsernameAndIsEnabled(username , true)
                .map(user ->  UserMapper.INSTANCE.toDto(user, new UserListDTO()));
    }

    @Override
    public List<UserListDTO> listOutstandingUser(){
        return userRepository.findByStatusAndApprovedByIsNotNull(Constants.UserStatus.APPROVE_NEW).stream()
                .map(user -> UserMapper.INSTANCE.toDto(user, new UserListDTO())).collect(Collectors.toCollection(LinkedList::new));

    }

    @Override
    @Transactional(readOnly = true)
    public List<UserListDTO> findAllUser(String status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();
        Optional<User> users = userRepository.findOneByUsernameAndIsEnabled(userLogin,true);
        User userLoggedIn = users.get();
        String validateRole = userLoggedIn.getRoles().get(0).getCode();
        List<User> userList = null;

        if (status == null || status == "" || status.equalsIgnoreCase("null")){
            if(validateRole.equals(Constants.Role.BANK_ADMIN_2_MAKER)){
                userList = userRepository.getUserListBenfMaker(validateRole);
            }
            else if(validateRole.equals(Constants.Role.BANK_ADMIN_2_CHECKER)){
                userList = userRepository.getUserListBenfChecker(Constants.Role.BANK_ADMIN_2_MAKER);
            }
            else if(validateRole.equals(Constants.Role.BENEFICIARY_ADMIN_1) ){
                userList = userRepository.getUserListExternalMaker(validateRole, userLoggedIn.getBeneficiary());
            } else if(validateRole.equals(Constants.Role.BENEFICIARY_ADMIN_2) ){
                userList = userRepository.getUserListExternalChecker(Constants.Role.BENEFICIARY_ADMIN_1, userLoggedIn.getBeneficiary());
            } else if(validateRole.equals(Constants.Role.BANK_ADMIN_1_MAKER)){
                userList = userRepository.getUserListMaker(validateRole);
            }
            else if(validateRole.equals(Constants.Role.BANK_ADMIN_1_CHECKER)){
                userList = userRepository.getUserListChecker(Constants.Role.BANK_ADMIN_1_MAKER);
            }
            else{
                userList = userRepository.getUserList(validateRole);
            }
        }else {
            if(validateRole.equals(Constants.Role.BANK_ADMIN_2_MAKER)
                    || validateRole.equals(Constants.Role.BANK_ADMIN_2_CHECKER)){
                userList = userRepository.getUserListGlobalDashboard(Constants.Role.BANK_ADMIN_2_MAKER, status);
            }else if(validateRole.equals(Constants.Role.BENEFICIARY_ADMIN_2)
                    || validateRole.equals(Constants.Role.BENEFICIARY_ADMIN_1)){
                userList = userRepository.getUserListBeneficiaryDashboard(validateRole, userLoggedIn.getBeneficiary(), status);
            }else if(validateRole.equals(Constants.Role.BANK_ADMIN_1_MAKER)
                    || validateRole.equals(Constants.Role.BANK_ADMIN_1_CHECKER)){
                userList = userRepository.getUserListGlobalDashboard(Constants.Role.BANK_ADMIN_1_MAKER, status);
            }else if (validateRole.equals(Constants.Role.IT)){
                userList = userRepository.getUserListITDashboard(validateRole, status, authentication.getName());
            }else{
                throw new BadRequestAlertException("Status Not Valid","","");
            }
        }

        return userList.stream()
                .map(user -> UserMapper.INSTANCE.toDto(user , new UserListDTO()))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    //Jika dia proses aktivasi set activated = true
    @Override
    @Transactional(readOnly = true)
    public Boolean userApproval(Long userId, String approvalType, Boolean approve) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userApproval = userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("User Not Found"));
        User oldUser = (User) SerializationUtils.clone(userApproval);
        List<String> validasi = null;
        validasi = userValidator.approvalValidator(userApproval, approvalType, approve);
        userApproval.setEnabled(Boolean.parseBoolean(validasi.get(0)));
        userApproval.setStatus(validasi.get(1));
        userRepository.save(userApproval);

        //TODO AUDIT TRAIL
        auditTrailUtil.saveAudit(
                validasi.get(2),
                Constants.Module.USER,
                new JSONObject(oldUser).toString(),
                new JSONObject(userApproval).toString(),
                validasi.get(3),
                authentication.getName()
        );
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean approveOrRejectUser(ApprovalUserDTO approvalUserDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        Optional<User> users = userRepository.findOneByUsernameAndIsEnabled(userLogin,true);
        User userInAction = users.get();

        Optional<User> user = userRepository.findOneByIdAndIsEnabled(approvalUserDTO.getUserId() , true);
        User userNeedApprove = user.get();
        User oldUser = (User) SerializationUtils.clone(userNeedApprove);


        if(userNeedApprove.getStatus().equals(Constants.UserStatus.CREATE_NEW)){
            if(approvalUserDTO.getAction().equals(Constants.action.APPROVE)){
                userNeedApprove.setStatus(Constants.UserStatus.APPROVE_NEW);
                userNeedApprove.setEnabled(Boolean.TRUE);
                userNeedApprove.setApprovedBy(userLogin);
                userNeedApprove.setApprovedDate(Instant.now());
                userNeedApprove.setUpdateManualBy(userInAction.getUsername());
                User userSaved = userRepository.save(userNeedApprove);
                userUtil.userHistory(userSaved , userLogin,Constants.action.APPROVED_USER_BY_CHECKER, null);

                //add column
                ColumnUserDTO columnUserDTO = new ColumnUserDTO();
                columnUserDTO.setUsername(userNeedApprove.getUsername());
                columnUserDTO.setListColumn(Constants.ColumnDetail.COLUMN_LIST);
                columnUserService.saveColumnUser(columnUserDTO);

                //TODO AUDIT TRAIL
                auditTrailUtil.saveAudit(
                        Constants.Event.ACTIVATED,
                        Constants.Module.USER,
                        new JSONObject(oldUser).toString(),
                        new JSONObject(userNeedApprove).toString(),
                        Constants.Remark.CREATE_USER_STATUS_APPROVED,
                        authentication.getName()
                );

            }else if(approvalUserDTO.getAction().equals(Constants.action.REJECT)){
                userNeedApprove.setEnabled(Boolean.TRUE);
                userNeedApprove.setNote(approvalUserDTO.getNote());
                userNeedApprove.setStatus(Constants.UserStatus.REJECT_NEW);
                userNeedApprove.setUpdateManualBy(userInAction.getUsername());
                User userSaved =  userRepository.save(userNeedApprove);
                userUtil.userHistory(userSaved , userLogin,Constants.action.REJECTED_USER_BY_CHECKER, approvalUserDTO.getNote());

                //TODO AUDIT TRAIL
                auditTrailUtil.saveAudit(
                        Constants.Event.ACTIVATED,
                        Constants.Module.USER,
                        new JSONObject(oldUser).toString(),
                        new JSONObject(userNeedApprove).toString(),
                        Constants.Remark.CREATE_USER_STATUS_REJECTED,
                        authentication.getName()
                );
            }
        }else if(userNeedApprove.getStatus().equals(Constants.UserStatus.REQ_DELETED)){
            if(approvalUserDTO.getAction().equals(Constants.action.APPROVE)){
                userNeedApprove.setStatus(Constants.UserStatus.DELETED);
                userNeedApprove.setEnabled(Boolean.FALSE);
                userNeedApprove.setApprovedDate(Instant.now());
                userNeedApprove.setApprovedBy(userInAction.getUsername());
                userNeedApprove.setUpdateManualBy(userInAction.getUsername());
                User userSaved =  userRepository.save(userNeedApprove);
                userUtil.userHistory(userSaved , userLogin,Constants.action.DELETED_USER_BY_CHECKER, null);

                //TODO AUDIT TRAIL
                auditTrailUtil.saveAudit(
                        Constants.Event.DELETE,
                        Constants.Module.USER,
                        new JSONObject(oldUser).toString(),
                        new JSONObject(userNeedApprove).toString(),
                        Constants.Remark.DELETE_USER_STATUS_APPROVED,
                        authentication.getName()
                );
            }else{
                userNeedApprove.setEnabled(Boolean.TRUE);
                userNeedApprove.setNote(approvalUserDTO.getNote());
                userNeedApprove.setStatus(Constants.UserStatus.APPROVE_NEW);
                userNeedApprove.setUpdateManualBy(userInAction.getUsername());
                User userSaved =  userRepository.save(userNeedApprove);
                userUtil.userHistory(userSaved , userLogin,Constants.action.REJECTED_DELETION_BY_CHECKER, approvalUserDTO.getNote());

                //TODO AUDIT TRAIL
                auditTrailUtil.saveAudit(
                        Constants.Event.DELETE,
                        Constants.Module.USER,
                        new JSONObject(oldUser).toString(),
                        new JSONObject(userNeedApprove).toString(),
                        Constants.Remark.DELETE_USER_STATUS_REJECTED,
                        authentication.getName()
                );
            }
        }else if(userNeedApprove.getStatus().equals(Constants.UserStatus.REQ_RESET_PASSWORD)){
            if(approvalUserDTO.getAction().equals(Constants.action.APPROVE)){

                Optional<MasterConfiguration> masterConfiguration = masterConfigurationRepository.findTop1ByStatusOrderByCreationDateDesc("ACTIVE");
                if(!masterConfiguration.isPresent()){
                    throw new AjoException("Default password belum tersedia ,  silahkan hubungi Super Admin");
                }

                String encryptedPassword = new BCryptPasswordEncoder().encode(masterConfiguration.get().getPassword());
                userNeedApprove.setUpdateManualBy(userInAction.getUsername());
                userNeedApprove.setPassword(encryptedPassword);
                userNeedApprove.setStatus(Constants.UserStatus.APPROVE_NEW);
                userNeedApprove.setIsForceChangePassword(Boolean.TRUE);
                User userSaved =  userRepository.saveAndFlush(userNeedApprove);
                userUtil.userHistory(userSaved , userLogin,Constants.action.APPROVED_PASSWORD_RESET_BY_MAKER, approvalUserDTO.getNote());

                //TODO AUDIT TRAIL
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.USER,
                        new JSONObject(oldUser).toString(),
                        new JSONObject(userNeedApprove).toString(),
                        Constants.Remark.RESET_PASSWORD_STATUS_APPROVED,
                        authentication.getName()
                );

            }else if(approvalUserDTO.getAction().equals(Constants.action.REJECT)){
                userNeedApprove.setUpdateManualBy(userInAction.getUsername());
                userNeedApprove.setStatus(Constants.UserStatus.APPROVE_NEW);
                userNeedApprove.setNote(approvalUserDTO.getNote());
                User userSaved =  userRepository.save(userNeedApprove);
                userUtil.userHistory(userSaved , userLogin,Constants.action.REJECTED_PASSWORD_RESET_BY_MAKER, approvalUserDTO.getNote());

                //TODO AUDIT TRAIL
                auditTrailUtil.saveAudit(
                        Constants.Event.UPDATE,
                        Constants.Module.USER,
                        new JSONObject(oldUser).toString(),
                        new JSONObject(userNeedApprove).toString(),
                        Constants.Remark.RESET_PASSWORD_STATUS_REJECTED,
                        authentication.getName()
                );
            }
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean countDisabled(String username){
        Optional<User> user = userRepository.findOneByUsernameAndIsEnabled(username,true);
        if(!user.isPresent()){
            throw new AjoException("Username tidak ditemukan");

        }
        User users = user.get();
        if(users.getCountDisabled() == 4){
            users.setAccountNonLocked(Boolean.FALSE);
            users.setStatus(Constants.UserStatus.LOCKED);
            userRepository.save(users);
        }else{
            Integer count = users.getCountDisabled();
            users.setCountDisabled(count+1);
            userRepository.save(users);
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean unlockUser(String username){
        log.info("username : " + username);
        Optional<User> user = userRepository.findOneByUsernameAndIsEnabled(username, true);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        if(!user.isPresent()){
            throw new AjoException("Username tidak ditemukan");
        }
        User users = user.get();
        User oldUser = (User) SerializationUtils.clone(users);
        users.setCountDisabled(0);
        users.setUpdateManualBy(userLogin);
        users.setAccountNonLocked(Boolean.TRUE);
        users.setStatus(Constants.UserStatus.APPROVE_NEW);
        User userSaved =  userRepository.save(users);
        userUtil.userHistory(userSaved, userLogin,Constants.action.UNLOCK_USER, null);

        //TODO AUDIT TRAIL
        auditTrailUtil.saveAudit(
                Constants.Event.ACTIVATED,
                Constants.Module.USER,
                new JSONObject(oldUser).toString(),
                new JSONObject(users).toString(),
                Constants.Remark.UNLOCKED_ACCOUNT,
                userLogin
        );
        return Boolean.TRUE;
    }


    @Override
    @Transactional
    public Boolean resetPassword(String username){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();
        Optional<User> user= userRepository.findOneByUsernameAndIsEnabled(username,true);
        if (!user.isPresent()){
            throw new AjoException("username tidak ditemukan");
        }

        if(user.get().getStatus().equals(Constants.UserStatus.REQ_RESET_PASSWORD)){
            throw new AjoException("Status user sudah di Reset Password");
        }
        User oldUser = (User) SerializationUtils.clone(user.get());
        user.get().setStatus(Constants.UserStatus.REQ_RESET_PASSWORD);
        user.get().setUpdateManualBy(userLogin);
        userUtil.userHistory(user.get(), userLogin,Constants.action.PASSWORD_RESET_BY_MAKER, null);
        userRepository.saveAndFlush(user.get());

        //TODO AUDIT TRAIL
        auditTrailUtil.saveAudit(
                Constants.Event.UPDATE,
                Constants.Module.USER,
                new JSONObject(oldUser).toString(),
                new JSONObject(user.get()).toString(),
                Constants.Remark.RESET_PASSWORD_WAITING_APPROVAL,
                userLogin
        );
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public List<UserHistoryDTO> userHistory(Long id){
        Optional<User> user  = userRepository.findOneByIdAndIsEnabled(id,true);
        return userHistoryRepository.findByUserOrderByCreationDateAsc(user.get())
                .stream()
                .map(userHistory -> UserHistoryMapper.INSTANCE.toDto(userHistory ,new UserHistoryDTO()))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public void createLogoutAuditTrail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        auditTrailUtil.saveAudit( Constants.Event.LOGOUT,
                Constants.Module.USER,
                null,
                null,
                Constants.Remark.LOGOUT,
                authentication.getName()
        );
    }

    @Override
    @Transactional
    public Boolean resetCountDisabled(String username) {
        Optional<User> user= userRepository.findOneByUsernameAndIsEnabled(username,true);
        user.get().setCountDisabled(0);
        return Boolean.TRUE;
    }
}
