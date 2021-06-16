package com.ebizcipta.ajo.api.util;

import com.ebizcipta.ajo.api.domain.*;
import com.ebizcipta.ajo.api.exception.ResourceNotFoundException;
import com.ebizcipta.ajo.api.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@PropertySource("classpath:data.properties")
public class Bootstrap implements CommandLineRunner {

    //MENU VARIABLE
    @Value("#{'${menu.name}'.split(',')}")
    private List<String> menuName;
    @Value("#{'${menu.head}'.split(',')}")
    private List<String> menuHead;
    @Value("#{'${menu.alias}'.split(',')}")
    private List<String> menuAlias;


    //ROLE VARIABLE
    @Value("${role.nsb.name}")
    private String roleSuperName;
    @Value("#{'${role.nsb.menu}'.split(',')}")
    private List<String> roleSuperMenu;
    @Value("#{'${role.nsb.privilege}'.split(',')}")
    private List<String> roleItPrivilege;
    @Value("${role.nsb.role_create}")
    private String roleSuperCreate;
    @Value("${role.nsb.code}")
    private String roleSuperCode;

    @Value("${role.ba.maker.name}")
    private String roleBa1MakerName;
    @Value("#{'${role.ba.maker.menu}'.split(',')}")
    private List<String> roleBa1MakerMenu;
    @Value("#{'${role.ba.maker.privilege}'.split(',')}")
    private List<String> roleBa1MakerPrivilege;
    @Value("${role.ba.maker.role_create}")
    private String roleBa1MakerRoleCreate;
    @Value("${role.ba.maker.code}")
    private String roleBa1MakerCode;

    @Value("${role.ba.checker.name}")
    private String roleBa1CheckerName;
    @Value("#{'${role.ba.checker.menu}'.split(',')}")
    private List<String> roleBa1CheckerMenu;
    @Value("#{'${role.ba.checker.privilege}'.split(',')}")
    private List<String> roleBa1CheckerPrivilege;
    @Value("${role.ba.checker.role_create}")
    private String roleBa1CheckerRoleCreate;
    @Value("${role.ba.checker.code}")
    private String roleBa1CheckerCode;

    @Value("${role.tro.maker.name}")
    private String roleTroMakerName;
    @Value("#{'${role.tro.maker.menu}'.split(',')}")
    private List<String> roleTroMakerMenu;
    @Value("#{'${role.tro.maker.privilege}'.split(',')}")
    private List<String> roleTroMakerPrivilege;
    @Value("${role.tro.maker.role_create}")
    private String roleTroMakerRoleCreate;
    @Value("${role.tro.maker.code}")
    private String roleTroMakerCode;

    @Value("${role.tro.checker.name}")
    private String roleTroCheckerName;
    @Value("#{'${role.tro.checker.menu}'.split(',')}")
    private List<String> roleTroCheckerMenu;
    @Value("#{'${role.tro.checker.privilege}'.split(',')}")
    private List<String> roleTroCheckerPrivilege;
    @Value("${role.tro.checker.role_create}")
    private String roleTroCheckerRoleCreate;
    @Value("${role.tro.checker.code}")
    private String roleTroCheckerCode;

    @Value("${role.reviewer.name}")
    private String roleReviewerName;
    @Value("#{'${role.reviewer.menu}'.split(',')}")
    private List<String> roleReviewerMenu;
    @Value("#{'${role.reviewer.privilege}'.split(',')}")
    private List<String> roleReviewerPrivilege;
    @Value("${role.reviewer.role_create}")
    private String roleReviewerRoleCreate;
    @Value("${role.reviewer.code}")
    private String roleReviewerCode;

    @Value("${role.beneficiary.user.name}")
    private String roleBeneficiaryUserName;
    @Value("#{'${role.beneficiary.user.menu}'.split(',')}")
    private List<String> roleBeneficiaryUserMenu;
    @Value("#{'${role.beneficiary.user.privilege}'.split(',')}")
    private List<String> roleBeneficiaryUserPrivilege;
    @Value("${role.beneficiary.user.role_create}")
    private String roleBeneficiaryUserRoleCreate;
    @Value("${role.beneficiary.user.code}")
    private String roleBeneficiaryUserCode;


    //USER VARIABLE
    @Value("${bam.user.username}")
    private String ba1mUsername;
    @Value("${bam.user.password}")
    private String ba1mPassword;
    @Value("${bam.user.email}")
    private String ba1mEmail;
    @Value("${bam.user.first.name}")
    private String ba1mFirstName;
    @Value("${bam.user.last.name}")
    private String ba1mLastName;

    @Value("${bac.user.username}")
    private String ba1cUsername;
    @Value("${bac.user.password}")
    private String ba1cPassword;
    @Value("${bac.user.email}")
    private String ba1cEmail;
    @Value("${bac.user.first.name}")
    private String ba1cFirstName;
    @Value("${bac.user.last.name}")
    private String ba1cLastName;

    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final BCryptPasswordEncoder encoder;
    private final PrivilegeUtil privilegeUtil;
    private final MessageSource messageSource;
    private final Environment environment;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final MasterData masterData;

    public Bootstrap(UserRepository userRepository, MenuRepository menuRepository, RoleRepository roleRepository,
                     PrivilegeRepository privilegeRepository, BCryptPasswordEncoder encoder,
                     PrivilegeUtil privilegeUtil, MessageSource messageSource, Environment environment,
                     PasswordHistoryRepository passwordHistoryRepository, MasterData masterData) {
        this.userRepository = userRepository;
        this.menuRepository = menuRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.encoder = encoder;
        this.privilegeUtil = privilegeUtil;
        this.messageSource = messageSource;
        this.environment = environment;
        this.passwordHistoryRepository = passwordHistoryRepository;
        this.masterData = masterData;
    }

    @Override
    public void run(String... args) throws Exception {
//        initMenu();
//        initRoleIT();
//        initRoleBAMaker();
//        initRoleBAChecker();
//        initRoleTROMaker();
//        initRoleTROChecker();
//        initRoleReviewer();
//        initRoleBeneficiaryUser();
//        initBa1c();
//        initBa1m();
//        masterData.insertMasterData();
    }


    //INIT MENU
    private void initMenu() {
        // save menu name
        menuName.forEach(s -> {
            if (!menuRepository.findByNameLikeIgnoreCase(s).isPresent()) {
                Menu menu = new Menu();
                menu.setName(s);
                menuRepository.save(menu);
            }
        });

        // save menu head name
        List<Menu> menus = menuRepository.findAll();

        int index = 0;
        for (Menu menu : menus) {
            menu.setHeadMenu(menuHead.get(index));
            menu.setAliasMenu(menuAlias.get(index));
            menuRepository.save(menu);
            index++;
        }
    }


    //INIT ROLE
    private void initRoleIT() throws ResourceNotFoundException {
        List<Privilege> privileges = new ArrayList<>();

        // get privileges
        int i = 0;
        for (String menu : roleSuperMenu) {
            Privilege privilege = privilegeUtil.getPrivilege(menu, roleItPrivilege.get(i));

            privilege.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
            privilege.setApprovedDate(Instant.now());

            Privilege addPrivilege = privilegeRepository.save(privilege);
            privileges.add(addPrivilege);

            i++;
        }

        // check role
        Role role = roleRepository.findByNameLikeIgnoreCase(roleSuperName).orElseGet(Role::new);

        if (role.getName() == null) role.setName(roleSuperName);
        role.setActivated(Boolean.TRUE);
        role.setRoleCreate(roleSuperCreate);
        role.setCode(roleSuperCode);
        role.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
        role.setApprovedDate(Instant.now());
        role.setStatus(Constants.RoleStatus.ACTIVE);
        role.setPrivileges(privileges);

        roleRepository.save(role);
    }

    private void initRoleBAMaker() throws ResourceNotFoundException {
        List<Privilege> privileges = new ArrayList<>();

        // get privileges
        int i = 0;
        for (String menu : roleBa1MakerMenu) {
            Privilege privilege = privilegeUtil.getPrivilege(menu, roleBa1MakerPrivilege.get(i));

            privilege.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
            privilege.setApprovedDate(Instant.now());

            Privilege addPrivilege = privilegeRepository.save(privilege);
            privileges.add(addPrivilege);

            i++;
        }

        // check role
        Role role = roleRepository.findByNameLikeIgnoreCase(roleBa1MakerName).orElseGet(Role::new);

        if (role.getName() == null) role.setName(roleBa1MakerName);
        role.setActivated(Boolean.TRUE);
        role.setRoleCreate(roleBa1CheckerRoleCreate);
        role.setCode(roleBa1MakerCode);
        role.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
        role.setApprovedDate(Instant.now());
        role.setStatus(Constants.RoleStatus.ACTIVE);
        role.setPrivileges(privileges);

        roleRepository.save(role);
    }

    private void initRoleBAChecker() throws ResourceNotFoundException {
        List<Privilege> privileges = new ArrayList<>();

        // get privileges
        int i = 0;
        for (String menu : roleBa1CheckerMenu) {
            Privilege privilege = privilegeUtil.getPrivilege(menu, roleBa1CheckerPrivilege.get(i));

            privilege.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
            privilege.setApprovedDate(Instant.now());

            Privilege addPrivilege = privilegeRepository.save(privilege);
            privileges.add(addPrivilege);

            i++;
        }

        // check role
        Role role = roleRepository.findByNameLikeIgnoreCase(roleBa1CheckerName).orElseGet(Role::new);

        if (role.getName() == null) role.setName(roleBa1CheckerName);
        role.setActivated(Boolean.TRUE);
        role.setRoleCreate(roleBa1CheckerRoleCreate);
        role.setCode(roleBa1CheckerCode);
        role.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
        role.setApprovedDate(Instant.now());
        role.setStatus(Constants.RoleStatus.ACTIVE);
        role.setPrivileges(privileges);

        roleRepository.save(role);
    }

    private void initRoleTROMaker() throws ResourceNotFoundException {
        List<Privilege> privileges = new ArrayList<>();

        // get privileges
        int i = 0;
        for (String menu : roleTroMakerMenu) {
            Privilege privilege = privilegeUtil.getPrivilege(menu, roleTroMakerPrivilege.get(i));

            privilege.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
            privilege.setApprovedDate(Instant.now());

            Privilege addPrivilege = privilegeRepository.save(privilege);
            privileges.add(addPrivilege);

            i++;
        }

        // check role
        Role role = roleRepository.findByNameLikeIgnoreCase(roleTroMakerName).orElseGet(Role::new);

        if (role.getName() == null) role.setName(roleTroMakerName);
        role.setActivated(Boolean.TRUE);
        role.setRoleCreate(roleTroMakerRoleCreate);
        role.setCode(roleTroMakerCode);
        role.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
        role.setApprovedDate(Instant.now());
        role.setStatus(Constants.RoleStatus.ACTIVE);
        role.setPrivileges(privileges);

        roleRepository.save(role);
    }

    private void initRoleTROChecker() throws ResourceNotFoundException {
        List<Privilege> privileges = new ArrayList<>();

        // get privileges
        int i = 0;
        for (String menu : roleTroCheckerMenu) {
            Privilege privilege = privilegeUtil.getPrivilege(menu, roleTroCheckerPrivilege.get(i));

            privilege.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
            privilege.setApprovedDate(Instant.now());

            Privilege addPrivilege = privilegeRepository.save(privilege);
            privileges.add(addPrivilege);

            i++;
        }

        // check role
        Role role = roleRepository.findByNameLikeIgnoreCase(roleTroCheckerName).orElseGet(Role::new);

        if (role.getName() == null) role.setName(roleTroCheckerName);
        role.setActivated(Boolean.TRUE);
        role.setRoleCreate(roleTroCheckerRoleCreate);
        role.setCode(roleTroCheckerCode);
        role.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
        role.setApprovedDate(Instant.now());
        role.setStatus(Constants.RoleStatus.ACTIVE);
        role.setPrivileges(privileges);

        roleRepository.save(role);
    }

    private void initRoleReviewer() throws ResourceNotFoundException {
        List<Privilege> privileges = new ArrayList<>();

        // get privileges
        int i = 0;
        for (String menu : roleReviewerMenu) {
            Privilege privilege = privilegeUtil.getPrivilege(menu, roleReviewerPrivilege.get(i));

            privilege.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
            privilege.setApprovedDate(Instant.now());

            Privilege addPrivilege = privilegeRepository.save(privilege);
            privileges.add(addPrivilege);

            i++;
        }

        // check role
        Role role = roleRepository.findByNameLikeIgnoreCase(roleReviewerName).orElseGet(Role::new);

        if (role.getName() == null) role.setName(roleReviewerName);
        role.setActivated(Boolean.TRUE);
        role.setRoleCreate(roleReviewerRoleCreate);
        role.setCode(roleReviewerCode);
        role.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
        role.setApprovedDate(Instant.now());
        role.setStatus(Constants.RoleStatus.ACTIVE);
        role.setPrivileges(privileges);

        roleRepository.save(role);
    }

    private void initRoleBeneficiaryUser() throws ResourceNotFoundException {
        List<Privilege> privileges = new ArrayList<>();

        // get privileges
        int i = 0;
        for (String menu : roleBeneficiaryUserMenu) {
            Privilege privilege = privilegeUtil.getPrivilege(menu, roleBeneficiaryUserPrivilege.get(i));

            privilege.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
            privilege.setApprovedDate(Instant.now());

            Privilege addPrivilege = privilegeRepository.save(privilege);
            privileges.add(addPrivilege);

            i++;
        }

        // check role
        Role role = roleRepository.findByNameLikeIgnoreCase(roleBeneficiaryUserName).orElseGet(Role::new);

        if (role.getName() == null) role.setName(roleBeneficiaryUserName);
        role.setActivated(Boolean.TRUE);
        role.setRoleCreate(roleBeneficiaryUserRoleCreate);
        role.setCode(roleBeneficiaryUserCode);
        role.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
        role.setApprovedDate(Instant.now());
        role.setStatus(Constants.RoleStatus.ACTIVE);
        role.setPrivileges(privileges);

        roleRepository.save(role);
    }

    //INIT USER
    private void initBa1m() throws ResourceNotFoundException {
        // get role
        Role role = roleRepository.findByNameLikeIgnoreCase(roleBa1MakerName)
                .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage("error.not.found",
                        new String[]{"Role " + roleBa1MakerName},
                        LocaleContextHolder.getLocale())));
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        // check if Super Admin is empty
        if (!userRepository.findOneByUsername(ba1mUsername).isPresent()) {

            // save user
            User itUser = new User();
            itUser.setUsername(ba1mUsername);
            itUser.setPassword(encoder.encode(ba1mPassword));
            itUser.setEmail(ba1mEmail);
            itUser.setFirstName(ba1mFirstName);
            itUser.setLastName(ba1mLastName);
            itUser.setEnabled(true);
            itUser.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
            itUser.setApprovedDate(Instant.now());
            itUser.setNeedApprovalOrReject(Constants.Role.BANK_ADMIN_1_CHECKER);
            itUser.setStatus(Constants.UserStatus.APPROVE_NEW);
            itUser.setRoles(roles);
            itUser.setUpdateManualBy(Constants.User.SYSTEM_ACCOUNT);

            User userSaved =userRepository.save(itUser);
            PasswordHistory passwordHistory = new PasswordHistory();
            passwordHistory.setUser(userSaved);
            passwordHistory.setPassword(encoder.encode(ba1mPassword));
            passwordHistoryRepository.save(passwordHistory);

        }


    }

    private void initBa1c() throws ResourceNotFoundException {
        // get role
        Role role = roleRepository.findByNameLikeIgnoreCase(roleBa1CheckerName)
                .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage("error.not.found",
                        new String[]{"Role " + roleBa1CheckerName},
                        LocaleContextHolder.getLocale())));
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        // check if Super Admin is empty
        if (!userRepository.findOneByUsername(ba1cUsername).isPresent()) {

            // save user
            User itUser = new User();
            itUser.setUsername(ba1cUsername);
            itUser.setPassword(encoder.encode(ba1cPassword));
            itUser.setEmail(ba1cEmail);
            itUser.setFirstName(ba1cFirstName);
            itUser.setLastName(ba1cLastName);
            itUser.setEnabled(true);
            itUser.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
            itUser.setStatus(Constants.UserStatus.APPROVE_NEW);
            itUser.setApprovedDate(Instant.now());
            itUser.setNeedApprovalOrReject(Constants.Role.BANK_ADMIN_1_CHECKER);
            itUser.setRoles(roles);
            itUser.setUpdateManualBy(Constants.User.SYSTEM_ACCOUNT);

            User userSaved =  userRepository.save(itUser);
            PasswordHistory passwordHistory = new PasswordHistory();
            passwordHistory.setUser(userSaved);
            passwordHistory.setPassword(encoder.encode(ba1cPassword));
            passwordHistoryRepository.save(passwordHistory);

        }
    }
}
