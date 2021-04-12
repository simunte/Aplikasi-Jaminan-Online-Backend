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
    @Value("${role.super.name}")
    private String roleSuperName;
    @Value("#{'${role.super.menu}'.split(',')}")
    private List<String> roleSuperMenu;
    @Value("#{'${role.super.privilege}'.split(',')}")
    private List<String> roleItPrivilege;
    @Value("${role.super.role_create}")
    private String roleSuperCreate;
    @Value("${role.super.code}")
    private String roleSuperCode;

    @Value("${role.ba1.maker.name}")
    private String roleBa1MakerName;
    @Value("#{'${role.ba1.maker.menu}'.split(',')}")
    private List<String> roleBa1MakerMenu;
    @Value("#{'${role.ba1.maker.privilege}'.split(',')}")
    private List<String> roleBa1MakerPrivilege;
    @Value("${role.ba1.maker.role_create}")
    private String roleBa1MakerRoleCreate;
    @Value("${role.ba1.maker.code}")
    private String roleBa1MakerCode;

    @Value("${role.ba1.checker.name}")
    private String roleBa1CheckerName;
    @Value("#{'${role.ba1.checker.menu}'.split(',')}")
    private List<String> roleBa1CheckerMenu;
    @Value("#{'${role.ba1.checker.privilege}'.split(',')}")
    private List<String> roleBa1CheckerPrivilege;
    @Value("${role.ba1.checker.role_create}")
    private String roleBa1CheckerRoleCreate;
    @Value("${role.ba1.checker.code}")
    private String roleBa1CheckerCode;

    @Value("${role.ba2.maker.name}")
    private String roleBa2MakerName;
    @Value("#{'${role.ba2.maker.menu}'.split(',')}")
    private List<String> roleBa2MakerMenu;
    @Value("#{'${role.ba2.maker.privilege}'.split(',')}")
    private List<String> roleBa2MakerPrivilege;
    @Value("${role.ba2.maker.role_create}")
    private String roleBa2MakerRoleCreate;
    @Value("${role.ba2.maker.code}")
    private String roleBa2MakerCode;

    @Value("${role.ba2.checker.name}")
    private String roleBa2CheckerName;
    @Value("#{'${role.ba2.checker.menu}'.split(',')}")
    private List<String> roleBa2CheckerMenu;
    @Value("#{'${role.ba2.checker.privilege}'.split(',')}")
    private List<String> roleBa2CheckerPrivilege;
    @Value("${role.ba2.checker.role_create}")
    private String roleBa2CheckerRoleCreate;
    @Value("${role.ba2.checker.code}")
    private String roleBa2CheckerCode;

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

    @Value("${role.beneficiary.admin1.name}")
    private String roleBeneficiaryAdmin1Name;
    @Value("#{'${role.beneficiary.admin1.menu}'.split(',')}")
    private List<String> roleBeneficiaryAdmin1Menu;
    @Value("#{'${role.beneficiary.admin1.privilege}'.split(',')}")
    private List<String> roleBeneficiaryAdmin1Privilege;
    @Value("${role.beneficiary.admin1.role_create}")
    private String roleBeneficiaryAdmin1RoleCreate;
    @Value("${role.beneficiary.admin1.code}")
    private String roleBeneficiaryAdmin1Code;

    @Value("${role.beneficiary.admin2.name}")
    private String roleBeneficiaryAdmin2Name;
    @Value("#{'${role.beneficiary.admin2.menu}'.split(',')}")
    private List<String> roleBeneficiaryAdmin2Menu;
    @Value("#{'${role.beneficiary.admin2.privilege}'.split(',')}")
    private List<String> roleBeneficiaryAdmin2Privilege;
    @Value("${role.beneficiary.admin2.role_create}")
    private String roleBeneficiaryAdmin2RoleCreate;
    @Value("${role.beneficiary.admin2.code}")
    private String roleBeneficiaryAdmin2Code;

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

    @Value("${role.admin.name}")
    private String roleAdminName;
    @Value("#{'${role.admin.menu}'.split(',')}")
    private List<String> roleAdminMenu;
    @Value("#{'${role.admin.privilege}'.split(',')}")
    private List<String> roleAdminPrivilege;

    //USER VARIABLE
    @Value("${ba1m.user.username}")
    private String ba1mUsername;
    @Value("${ba1m.user.password}")
    private String ba1mPassword;
    @Value("${ba1m.user.email}")
    private String ba1mEmail;
    @Value("${ba1m.user.first.name}")
    private String ba1mFirstName;
    @Value("${ba1m.user.last.name}")
    private String ba1mLastName;

    @Value("${ba1c.user.username}")
    private String ba1cUsername;
    @Value("${ba1c.user.password}")
    private String ba1cPassword;
    @Value("${ba1c.user.email}")
    private String ba1cEmail;
    @Value("${ba1c.user.first.name}")
    private String ba1cFirstName;
    @Value("${ba1c.user.last.name}")
    private String ba1cLastName;

    @Value("${tro.maker.username}")
    private String troMakerUsername;
    @Value("${tro.maker.password}")
    private String troMakerPassword;
    @Value("${tro.maker.email}")
    private String troMakerEmail;
    @Value("${tro.maker.first.name}")
    private String troMakerFirstName;
    @Value("${tro.maker.last.name}")
    private String troMakerLastName;

    @Value("${tro.checker.username}")
    private String troCheckerUsername;
    @Value("${tro.checker.password}")
    private String troCheckerPassword;
    @Value("${tro.checker.email}")
    private String troCheckerEmail;
    @Value("${tro.checker.first.name}")
    private String troCheckerFirstName;
    @Value("${tro.checker.last.name}")
    private String troCheckerLastName;

    @Value("${pln.username}")
    private String plnUsername;
    @Value("${pln.password}")
    private String plnPassword;
    @Value("${pln.email}")
    private String plnEmail;
    @Value("${pln.first.name}")
    private String plnFirstName;
    @Value("${pln.last.name}")
    private String plnLastName;

    @Value("${admin.username}")
    private String adminUsername;
    @Value("${admin.password}")
    private String adminPassword;
    @Value("${admin.email}")
    private String adminEmail;
    @Value("${admin.first.name}")
    private String adminFirstName;
    @Value("${admin.last.name}")
    private String adminLastName;

    @Value("${admin2.username}")
    private String admin2Username;
    @Value("${admin2.password}")
    private String admin2Password;
    @Value("${admin2.email}")
    private String admin2Email;
    @Value("${admin2.first.name}")
    private String admin2FirstName;
    @Value("${admin2.last.name}")
    private String admin2LastName;


    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final BCryptPasswordEncoder encoder;
    private final PrivilegeUtil privilegeUtil;
    private final MessageSource messageSource;
    private final Environment environment;
    private final PasswordHistoryRepository passwordHistoryRepository;

    public Bootstrap(UserRepository userRepository, MenuRepository menuRepository, RoleRepository roleRepository,
                     PrivilegeRepository privilegeRepository, BCryptPasswordEncoder encoder,
                     PrivilegeUtil privilegeUtil, MessageSource messageSource, Environment environment, PasswordHistoryRepository passwordHistoryRepository) {
        this.userRepository = userRepository;
        this.menuRepository = menuRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.encoder = encoder;
        this.privilegeUtil = privilegeUtil;
        this.messageSource = messageSource;
        this.environment = environment;
        this.passwordHistoryRepository = passwordHistoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        initMenu();
//        initRoleIT();
//        initRoleBA1Maker();
//        initRoleBA1Checker();
//        initRoleBA2Maker();
//        initRoleBA2Checker();
//        initRoleTROMaker();
//        initRoleTROChecker();
//        initRoleReviewer();
//        initRoleBenAdmin1();
//        initRoleBenAdmin2();
//        initRoleBeneficiaryUser();
//        initBa1c();
//        initBa1m();
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

    private void initRoleBA1Maker() throws ResourceNotFoundException {
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

    private void initRoleBA1Checker() throws ResourceNotFoundException {
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

    private void initRoleBA2Maker() throws ResourceNotFoundException {
        List<Privilege> privileges = new ArrayList<>();

        // get privileges
        int i = 0;
        for (String menu : roleBa2MakerMenu) {
            Privilege privilege = privilegeUtil.getPrivilege(menu, roleBa2MakerPrivilege.get(i));

            privilege.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
            privilege.setApprovedDate(Instant.now());

            Privilege addPrivilege = privilegeRepository.save(privilege);
            privileges.add(addPrivilege);

            i++;
        }

        // check role
        Role role = roleRepository.findByNameLikeIgnoreCase(roleBa2MakerName).orElseGet(Role::new);

        if (role.getName() == null) role.setName(roleBa2MakerName);
        role.setActivated(Boolean.TRUE);
        role.setRoleCreate(roleBa2MakerRoleCreate);
        role.setCode(roleBa2MakerCode);
        role.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
        role.setApprovedDate(Instant.now());
        role.setStatus(Constants.RoleStatus.ACTIVE);
        role.setPrivileges(privileges);

        roleRepository.save(role);
    }

    private void initRoleBA2Checker() throws ResourceNotFoundException {
        List<Privilege> privileges = new ArrayList<>();

        // get privileges
        int i = 0;
        for (String menu : roleBa2CheckerMenu) {
            Privilege privilege = privilegeUtil.getPrivilege(menu, roleBa2CheckerPrivilege.get(i));

            privilege.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
            privilege.setApprovedDate(Instant.now());

            Privilege addPrivilege = privilegeRepository.save(privilege);
            privileges.add(addPrivilege);

            i++;
        }

        // check role
        Role role = roleRepository.findByNameLikeIgnoreCase(roleBa2CheckerName).orElseGet(Role::new);

        if (role.getName() == null) role.setName(roleBa2CheckerName);
        role.setActivated(Boolean.TRUE);
        role.setRoleCreate(roleBa2CheckerRoleCreate);
        role.setCode(roleBa2CheckerCode);
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

    private void initRoleBenAdmin1() throws ResourceNotFoundException {
        List<Privilege> privileges = new ArrayList<>();

        // get privileges
        int i = 0;
        for (String menu : roleBeneficiaryAdmin1Menu) {
            Privilege privilege = privilegeUtil.getPrivilege(menu, roleBeneficiaryAdmin1Privilege.get(i));

            privilege.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
            privilege.setApprovedDate(Instant.now());

            Privilege addPrivilege = privilegeRepository.save(privilege);
            privileges.add(addPrivilege);

            i++;
        }

        // check role
        Role role = roleRepository.findByNameLikeIgnoreCase(roleBeneficiaryAdmin1Name).orElseGet(Role::new);

        if (role.getName() == null) role.setName(roleBeneficiaryAdmin1Name);
        role.setActivated(Boolean.TRUE);
        role.setRoleCreate(roleBeneficiaryAdmin1RoleCreate);
        role.setCode(roleBeneficiaryAdmin1Code);
        role.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
        role.setApprovedDate(Instant.now());
        role.setStatus(Constants.RoleStatus.ACTIVE);
        role.setPrivileges(privileges);

        roleRepository.save(role);
    }

    private void initRoleBenAdmin2() throws ResourceNotFoundException {
        List<Privilege> privileges = new ArrayList<>();

        // get privileges
        int i = 0;
        for (String menu : roleBeneficiaryAdmin2Menu) {
            Privilege privilege = privilegeUtil.getPrivilege(menu, roleBeneficiaryAdmin2Privilege.get(i));

            privilege.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
            privilege.setApprovedDate(Instant.now());

            Privilege addPrivilege = privilegeRepository.save(privilege);
            privileges.add(addPrivilege);

            i++;
        }

        // check role
        Role role = roleRepository.findByNameLikeIgnoreCase(roleBeneficiaryAdmin2Name).orElseGet(Role::new);

        if (role.getName() == null) role.setName(roleBeneficiaryAdmin2Name);
        role.setActivated(Boolean.TRUE);
        role.setRoleCreate(roleBeneficiaryAdmin2RoleCreate);
        role.setCode(roleBeneficiaryAdmin2Code);
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

    private void initRoleSuperAdmin() throws ResourceNotFoundException {
        List<Privilege> privileges = new ArrayList<>();

        // get privileges
        int i = 0;
        for (String menu : roleAdminMenu) {
            Privilege privilege = privilegeUtil.getPrivilege(menu, roleAdminPrivilege.get(i));

            privilege.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
            privilege.setApprovedDate(Instant.now());

            Privilege addPrivilege = privilegeRepository.save(privilege);
            privileges.add(addPrivilege);

            i++;
        }

        // check role
        Role role = roleRepository.findByNameLikeIgnoreCase(roleAdminName).orElseGet(Role::new);

        if (role.getName() == null) role.setName(roleAdminName);
        role.setActivated(Boolean.TRUE);

        role.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
        role.setApprovedDate(Instant.now());

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

    private void initTROMakerUser() throws ResourceNotFoundException {
        // get role
        Role role = roleRepository.findByNameLikeIgnoreCase(roleTroMakerName)
                .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage("error.not.found",
                        new String[]{"Role " + roleTroMakerName},
                        LocaleContextHolder.getLocale())));
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        // check if Super Admin is empty
        if (!userRepository.findOneByUsername(troMakerUsername).isPresent()) {

            // save user
            User user = new User();
            user.setUsername(troMakerUsername);
            user.setPassword(encoder.encode(troMakerPassword));
            user.setEmail(troMakerEmail);
            user.setFirstName(troMakerFirstName);
            user.setLastName(troMakerLastName);
            user.setEnabled(true);
            user.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
            user.setApprovedDate(Instant.now());
            user.setStatus(Constants.UserStatus.APPROVE_NEW);
            user.setNeedApprovalOrReject(Constants.Role.BANK_ADMIN_1_CHECKER);
            user.setRoles(roles);

            userRepository.save(user);
        }
    }

    private void initTROCheckerUser() throws ResourceNotFoundException {
        // get role
        Role role = roleRepository.findByNameLikeIgnoreCase(roleTroCheckerName)
                .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage("error.not.found",
                        new String[]{"Role " + roleTroCheckerName},
                        LocaleContextHolder.getLocale())));
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        // check if Super Admin is empty
        if (!userRepository.findOneByUsername(troCheckerUsername).isPresent()) {

            // save user
            User user = new User();
            user.setUsername(troCheckerUsername);
            user.setPassword(encoder.encode(troCheckerPassword));
            user.setEmail(troCheckerEmail);
            user.setFirstName(troCheckerFirstName);
            user.setLastName(troCheckerLastName);
            user.setEnabled(true);
            user.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
            user.setApprovedDate(Instant.now());
            user.setStatus(Constants.UserStatus.APPROVE_NEW);
            user.setNeedApprovalOrReject(Constants.Role.BANK_ADMIN_1_CHECKER);
            user.setRoles(roles);

            userRepository.save(user);
        }
    }
    private void initPlnUserUser() throws ResourceNotFoundException {
        // get role
        Role role = roleRepository.findByNameLikeIgnoreCase(roleBeneficiaryUserName)
                .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage("error.not.found",
                        new String[]{"Role " + roleBeneficiaryUserName},
                        LocaleContextHolder.getLocale())));
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        // check if Super Admin is empty
        if (!userRepository.findOneByUsername(plnUsername).isPresent()) {

            // save user
            User user = new User();
            user.setUsername(plnUsername);
            user.setPassword(encoder.encode(plnPassword));
            user.setEmail(plnEmail);
            user.setFirstName(plnFirstName);
            user.setLastName(plnLastName);
            user.setEnabled(true);
            user.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
            user.setApprovedDate(Instant.now());
            user.setStatus(Constants.UserStatus.APPROVE_NEW);
            user.setNeedApprovalOrReject(Constants.Role.BENEFICIARY_ADMIN_2);
            user.setRoles(roles);

            userRepository.save(user);
        }
    }

    private void initSuperAdminUser() throws ResourceNotFoundException {
        // get role
        Role role = roleRepository.findByNameLikeIgnoreCase(roleSuperName)
                .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage("error.not.found",
                        new String[]{"Role " + roleSuperName},
                        LocaleContextHolder.getLocale())));
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        // check if Super Admin is empty
        if (!userRepository.findOneByUsername(adminUsername).isPresent()) {

            // save user
            User itUser = new User();
            itUser.setUsername(adminUsername);
            itUser.setPassword(encoder.encode(adminPassword));
            itUser.setEmail(adminEmail);
            itUser.setFirstName(adminFirstName);
            itUser.setLastName(adminLastName);
            itUser.setEnabled(true);
            itUser.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
            itUser.setApprovedDate(Instant.now());
            itUser.setStatus(Constants.UserStatus.APPROVE_NEW);
            itUser.setRoles(roles);
            itUser.setUpdateManualBy(Constants.User.SYSTEM_ACCOUNT);

            userRepository.save(itUser);
        }
    }

    private void initSuperAdminUser2() throws ResourceNotFoundException {
        // get role
        Role role = roleRepository.findByNameLikeIgnoreCase(roleSuperName)
                .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage("error.not.found",
                        new String[]{"Role " + roleSuperName},
                        LocaleContextHolder.getLocale())));
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        // check if Super Admin is empty
        if (!userRepository.findOneByUsername(admin2Username).isPresent()) {

            // save user
            User itUser = new User();
            itUser.setUsername(admin2Username);
            itUser.setPassword(encoder.encode(admin2Password));
            itUser.setEmail(admin2Email);
            itUser.setFirstName(admin2FirstName);
            itUser.setLastName(admin2LastName);
            itUser.setEnabled(true);
            itUser.setApprovedBy(Constants.User.SYSTEM_ACCOUNT);
            itUser.setApprovedDate(Instant.now());
            itUser.setStatus(Constants.UserStatus.APPROVE_NEW);
            itUser.setRoles(roles);
            itUser.setUpdateManualBy(Constants.User.SYSTEM_ACCOUNT);

            userRepository.save(itUser);
        }
    }
}
