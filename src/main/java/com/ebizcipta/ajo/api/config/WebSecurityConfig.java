package com.ebizcipta.ajo.api.config;

import com.ebizcipta.ajo.api.config.properties.SecurityConfigProperties;
import com.ebizcipta.ajo.api.domain.User;
import com.ebizcipta.ajo.api.repositories.UserRepository;
import com.ebizcipta.ajo.api.service.dto.AuthTokenCustomDTO;
import com.ebizcipta.ajo.api.service.dto.MenuListDTO;
import com.ebizcipta.ajo.api.service.dto.RoleListDTO;
import com.ebizcipta.ajo.api.service.mapper.MenuListMapper;
import com.ebizcipta.ajo.api.service.mapper.RoleListMapper;
import com.ebizcipta.ajo.api.service.mapper.UserMapper;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpointAuthenticationFilter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(SecurityConfigProperties.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    private final SecurityConfigProperties securityConfigProperties;
    private final UserDetailsService userDetailsService;
    private final ClientDetailsService clientDetailsService;
    private final UserRepository userRepository;

    public WebSecurityConfig(SecurityConfigProperties securityConfigProperties, UserDetailsService userDetailsService, ClientDetailsService clientDetailsService, UserRepository userRepository) {
        this.securityConfigProperties = securityConfigProperties;
        this.userDetailsService = userDetailsService;
        this.clientDetailsService = clientDetailsService;
        this.userRepository = userRepository;
    }

    @Bean
    public OAuth2RequestFactory requestFactory() {
        CustomOauth2RequestFactory requestFactory = new CustomOauth2RequestFactory(clientDetailsService);
        requestFactory.setCheckUserScopes(true);

        return requestFactory;
    }

    @Bean
    BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    TokenStore tokenStore(){
        return new InMemoryTokenStore();
    }

    @Bean
    JwtAccessTokenConverter accessTokenConverter() {
//        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        final JwtAccessTokenConverter converter = new CustomTokenEnhancer();

        converter.setKeyPair(new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"),
                securityConfigProperties.getKeyStorePassword().toCharArray()).getKeyPair("jwt"));

        return converter;
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices
                = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        return tokenServices;
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requiresChannel()
                .anyRequest()
                .requiresSecure()
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .httpBasic()
                .realmName(securityConfigProperties.getSecurityRealm())
            .and()
                .anonymous().disable()
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(encoder());
    }
    @Bean
    public TokenEndpointAuthenticationFilter tokenEndpointAuthenticationFilter() {
        return new TokenEndpointAuthenticationFilter(authenticationManager, requestFactory());
    }

    class CustomTokenEnhancer extends JwtAccessTokenConverter {
        @Override
        public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
            Optional<User> user = userRepository.findOneByUsernameAndIsEnabled(authentication.getName(), true);
            Map<String, Object> info = new LinkedHashMap<>(accessToken.getAdditionalInformation());
            AuthTokenCustomDTO authTokenCustomDTO = UserMapper.INSTANCE.toDtoAuth(user.get(), new AuthTokenCustomDTO());
            List<RoleListDTO> roleList = user.get().getRoles().stream()
                    .map(role -> {
                        RoleListDTO roleListDTO = RoleListMapper.INSTANCE.toDto(role, new RoleListDTO());
                        List<MenuListDTO> menuListDTOS = role.getPrivileges().stream()
                                .map(privilege -> {
                                    MenuListDTO menuListDTO = MenuListMapper.INSTANCE.toDto(privilege.getMenu());
                                    menuListDTO.setCreate(privilege.getCreate());
                                    menuListDTO.setDelete(privilege.getDelete());
                                    menuListDTO.setRead(privilege.getRead());
                                    menuListDTO.setUpdate(privilege.getUpdate());
                                    menuListDTO.setApproval(privilege.getApproval());
                                    return menuListDTO;
                                }).collect(Collectors.toCollection(LinkedList::new));
                        roleListDTO.setMenuList(menuListDTOS);
                        return roleListDTO;
                    }).collect(Collectors.toCollection(LinkedList::new));
            authTokenCustomDTO.setRoles(roleList);
            info.put("users", authTokenCustomDTO);
            accessToken = super.enhance(accessToken, authentication);
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
            return accessToken;
        }
    }

    class CustomOauth2RequestFactory extends DefaultOAuth2RequestFactory {
        @Autowired
        private TokenStore tokenStore;

        public CustomOauth2RequestFactory(ClientDetailsService clientDetailsService) {
            super(clientDetailsService);
        }

        @Override
        public TokenRequest createTokenRequest(Map<String, String> requestParameters,
                                               ClientDetails authenticatedClient) {
            if (requestParameters.get("grant_type").equals("refresh_token")) {
                OAuth2Authentication authentication = tokenStore.readAuthenticationForRefreshToken(
                        tokenStore.readRefreshToken(requestParameters.get("refresh_token")));
                SecurityContextHolder.getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(authentication.getName(), null,
                                userDetailsService.loadUserByUsername(authentication.getName()).getAuthorities()));
            }
            return super.createTokenRequest(requestParameters, authenticatedClient);
        }
    }
}
