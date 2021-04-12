package com.ebizcipta.module.login;


import com.ebizcipta.ajo.api.ModuleApplication;
import com.ebizcipta.ajo.api.config.properties.SecurityConfigProperties;
import com.ebizcipta.ajo.api.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModuleApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class OAuthMvcTest {
    @Value("${user.admin.username}")
    private String username;

    @Value("${user.admin.password}")
    private String password;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private SecurityConfigProperties securityConfigProperties;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    private final String CSA_USER = "CSA_USER";
    private final String AYUNDA = "ayunda";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain).build();

        injectDataUser();
    }

    private void injectDataUser() {
//        List<Role> roles = new ArrayList<>();
//
//        // Role Admin
//        Role adminRole = new Role();
//        adminRole.setName(CSA_USER);
//        adminRole.setDescription("CSA User");
//        roles.add(adminRole);
//
//        // User
//        User user = new User();
//        user.setUsername(AYUNDA);
//        user.setFirstName("Ayunda");
//        user.setLastName("Oktavia");
//        user.setPassword(encoder.encode(password));
//        user.setRoles(roles);
//
//        User foundUser = userRepository.findOneByUsername(user.getUsername())
//                .orElseGet(() -> userRepository.save(user));
    }

//    private String obtainAccessToken(String username, String password) throws Exception {
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", securityConfigProperties.getJwt().getGrantType());
//        params.add("client_id", securityConfigProperties.getJwt().getClientId());
//        params.add("username", username);
//        params.add("password", password);
//
//        ResultActions result
//                = mockMvc.perform(post("/oauth/token")
//                .params(params)
//                .with(httpBasic(securityConfigProperties.getJwt().getClientId(),
//                        securityConfigProperties.getJwt().getClientSecret()))
//                .accept(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
//
//        String resultString = result.andReturn().getResponse().getContentAsString();
//
//        JacksonJsonParser jsonParser = new JacksonJsonParser();
//        return jsonParser.parseMap(resultString).get("access_token").toString();
//    }

//    @Test
//    public void givenNoToken_whenGetSecureRequest_thenUnauthorized() throws Exception {
//        mockMvc.perform(get("/api/v1"))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    public void givenInvalidRole_whenGetSecureRequest_thenForbidden() throws Exception {
//        final String accessToken = obtainAccessToken(AYUNDA, password);
//        mockMvc.perform(get("/api/v1")
//                .header("Authorization", "Bearer " + accessToken))
//                .andExpect(status().isForbidden());
//    }
//
    @Test
    public void givenToken_whenPostGetSecureRequest_thenOk() throws Exception {
//        final String accessToken = obtainAccessToken(username, password);
//        mockMvc.perform(get("/api/v1")
//                .header("Authorization", "Bearer " + accessToken))
//                .andExpect(status().isOk());
    }
}
