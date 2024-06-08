package org.etf.unibl.SecureForum.security;

import org.etf.unibl.SecureForum.model.entities.UserEntity;
import org.etf.unibl.SecureForum.model.enums.UserType;
import org.etf.unibl.SecureForum.repositories.UserRepository;
import org.etf.unibl.SecureForum.security.config.GoogleOpaqueTokenIntrospector;
import org.etf.unibl.SecureForum.security.xss_filter.XSSFilter;
import org.etf.unibl.SecureForum.security.xss_filter.XSSFilterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.http.HttpRequest;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig{

    private final JwtAuthEntryPoint authEntryPoint;
    private final AuthenticationProvider authenticationProvider;

    private final String ADMIN_ROLE = UserType.Administrator.getValue();
    private final String MODERATOR_ROLE = UserType.Moderator.getValue();
    private final String FORUMER_ROLE = UserType.Forumer.getValue();

    private final WebClient userInfoClient;

    @Autowired
    public WebSecurityConfig(AuthenticationProvider authenticationProvider,
                             JwtAuthEntryPoint authEntryPoint,
                            WebClient userInfoClient){
        this.authenticationProvider = authenticationProvider;
        this.authEntryPoint = authEntryPoint;
        this.userInfoClient = userInfoClient;

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(setting -> setting.disable())
                .exceptionHandling(handlingConfigurer -> {handlingConfigurer.authenticationEntryPoint(authEntryPoint);})
                .sessionManagement(sessionManagement -> {sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);})
                .authorizeHttpRequests(requestMatcherConfigurer -> {requestMatcherConfigurer.requestMatchers("/auth/**").permitAll();

                                                                    requestMatcherConfigurer.requestMatchers("/users/**").hasRole(ADMIN_ROLE); //Only administrator can manipulate with users

                                                                    requestMatcherConfigurer.requestMatchers(HttpMethod.POST, "/forum_post/add").hasAnyRole(ADMIN_ROLE, MODERATOR_ROLE,FORUMER_ROLE);
                                                                    requestMatcherConfigurer.requestMatchers(HttpMethod.PUT, "/forum_post/update").hasAnyRole(ADMIN_ROLE, MODERATOR_ROLE,FORUMER_ROLE);
                                                                    requestMatcherConfigurer.requestMatchers(HttpMethod.DELETE, "/forum_post/delete/**").hasAnyRole(ADMIN_ROLE, MODERATOR_ROLE,FORUMER_ROLE);

                                                                    requestMatcherConfigurer.requestMatchers(HttpMethod.GET, "/forum_post").hasAnyRole(ADMIN_ROLE, MODERATOR_ROLE);
                                                                    requestMatcherConfigurer.requestMatchers(HttpMethod.POST,"/forum_post/**").hasAnyRole(ADMIN_ROLE, MODERATOR_ROLE);
                                                                    requestMatcherConfigurer.requestMatchers(HttpMethod.PUT,"/forum_post/**").hasAnyRole(ADMIN_ROLE, MODERATOR_ROLE);
                                                                    requestMatcherConfigurer.requestMatchers(HttpMethod.DELETE,"/forum_post/**").hasAnyRole(ADMIN_ROLE, MODERATOR_ROLE);

                                                                    requestMatcherConfigurer.requestMatchers(HttpMethod.GET, "/comment/find_approved/**").hasAnyRole(ADMIN_ROLE, MODERATOR_ROLE,FORUMER_ROLE);
                                                                    requestMatcherConfigurer.requestMatchers(HttpMethod.POST, "/comment/add").hasAnyRole(ADMIN_ROLE, MODERATOR_ROLE,FORUMER_ROLE);
                                                                    requestMatcherConfigurer.requestMatchers(HttpMethod.PUT, "/comment/update").hasAnyRole(ADMIN_ROLE, MODERATOR_ROLE,FORUMER_ROLE);
                                                                    requestMatcherConfigurer.requestMatchers(HttpMethod.DELETE, "/comment/delete/**").hasAnyRole(ADMIN_ROLE, MODERATOR_ROLE,FORUMER_ROLE);

                                                                    requestMatcherConfigurer.requestMatchers(HttpMethod.GET, "/comment/**").hasAnyRole(ADMIN_ROLE, MODERATOR_ROLE);
                                                                    requestMatcherConfigurer.requestMatchers(HttpMethod.POST,"/comment/**").hasAnyRole(ADMIN_ROLE, MODERATOR_ROLE);
                                                                    requestMatcherConfigurer.requestMatchers(HttpMethod.PUT,"/comment/**").hasAnyRole(ADMIN_ROLE, MODERATOR_ROLE);
                                                                    requestMatcherConfigurer.requestMatchers(HttpMethod.DELETE,"/comment/**").hasAnyRole(ADMIN_ROLE, MODERATOR_ROLE);



                                                                    requestMatcherConfigurer.anyRequest().fullyAuthenticated(); //Only those who have logged in and have a token are allowed to do requests
                })
                .authenticationProvider(authenticationProvider);

        http.addFilterBefore(xssFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);


       // http.formLogin(Customizer.withDefaults());
       // http.oauth2ResourceServer(oauth2 -> oauth2.jwt(
       //         jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(customJwtConverter())
       // ));
        return http.build();
    }

    @Bean
    public Converter<Jwt,CustomJwt> customJwtConverter() {
        return new CustomJwtConverter();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter(){
        return new JWTAuthenticationFilter();
    }

    @Bean
    public XSSFilter xssFilter(){ return new XSSFilter();}

    @Bean
    public OpaqueTokenIntrospector introspector(){
        return new GoogleOpaqueTokenIntrospector(userInfoClient);
    }


}
