package com.ebizcipta.module.repositories;

import com.ebizcipta.ajo.api.ModuleApplication;
import com.ebizcipta.ajo.api.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModuleApplication.class)
@ActiveProfiles("test")
public class UserRepositoryTest {
    private final String ADMIN = "ADMIN_FAILED";
    private final String USER = "USER_FAILED";
    private final String OKTAVIA = "oktavia";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private MessageSource messageSource;

    @Value("${user.admin.password}")
    private String password;

//    private User injectDataUser() {
//        List<Role> roles = new ArrayList<>();
//
//        // Role Admin
//        Role adminRole = new Role();
//        adminRole.setName(ADMIN);
//        adminRole.setDescription("Admin failed role");
//        roles.add(adminRole);
//
//        // Role User
//        Role userRole = new Role();
//        userRole.setName(USER);
//        userRole.setDescription("User failed role");
//        roles.add(userRole);
//
//        // User
//        User user = new User();
//        user.setUsername(OKTAVIA);
//        user.setFirstName("Ayunda");
//        user.setLastName("Oktavia");
//        user.setPassword(encoder.encode(password));
//        user.setRoles(roles);
//
//        return userRepository.save(user);
//    }

    @Test
    public void insertDataUser_whenSearchUserByUsername_thenResultTrue() throws Exception {
//        // given
//        User user = injectDataUser();
//
//        // when
//        User found = userRepository.findOneByUsername(OKTAVIA)
//                .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage("error.not.found",
//                        new String[]{"Username"},
//                        LocaleContextHolder.getLocale())));
//
//        // then
//        assertThat(found.getFirstName() + " " + found.getLastName())
//                .isEqualTo(user.getFirstName() + " " + user.getLastName());
    }
}
