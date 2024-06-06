package org.etf.unibl.SecureForum.security;

import org.etf.unibl.SecureForum.model.entities.UserEntity;
import org.etf.unibl.SecureForum.model.enums.UserType;
import org.etf.unibl.SecureForum.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.net.http.HttpRequest;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig{

    private final JwtAuthEntryPoint authEntryPoint;
    private final AuthenticationProvider authenticationProvider;

    private final String ADMIN_ROLE = UserType.Administrator.getValue();
    private final String MODERATOR_ROLE = UserType.Moderator.getValue();
    private final String FORUMER_ROLE = UserType.Forumer.getValue();

    @Autowired
    public WebSecurityConfig(AuthenticationProvider authenticationProvider,
                             JwtAuthEntryPoint authEntryPoint){
        this.authenticationProvider = authenticationProvider;
        this.authEntryPoint = authEntryPoint;

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(setting -> setting.disable())
                .exceptionHandling(handlingConfigurer -> {handlingConfigurer.authenticationEntryPoint(authEntryPoint);})
                .sessionManagement(sessionManagement -> {sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);})
                .authorizeHttpRequests(requestMatcherConfigurer -> {requestMatcherConfigurer.requestMatchers("/users/**").hasRole(ADMIN_ROLE); //Only administrator can manipulate with users

                                                                    requestMatcherConfigurer.requestMatchers(HttpMethod.GET, "/forum_post").hasAnyRole(ADMIN_ROLE, MODERATOR_ROLE);
                                                                    requestMatcherConfigurer.requestMatchers(HttpMethod.POST,"/forum_post/**").hasAnyRole(ADMIN_ROLE, MODERATOR_ROLE);
                                                                    requestMatcherConfigurer.requestMatchers(HttpMethod.PUT,"/forum_post/**").hasAnyRole(ADMIN_ROLE, MODERATOR_ROLE);
                                                                    requestMatcherConfigurer.requestMatchers(HttpMethod.DELETE,"/forum_post/**").hasAnyRole(ADMIN_ROLE, MODERATOR_ROLE);

                                                                    requestMatcherConfigurer.requestMatchers(HttpMethod.GET, "/comment/**").hasAnyRole(ADMIN_ROLE, MODERATOR_ROLE);
                                                                    requestMatcherConfigurer.requestMatchers(HttpMethod.POST,"/comment/**").hasAnyRole(ADMIN_ROLE, MODERATOR_ROLE);
                                                                    requestMatcherConfigurer.requestMatchers(HttpMethod.PUT,"/comment/**").hasAnyRole(ADMIN_ROLE, MODERATOR_ROLE);
                                                                    requestMatcherConfigurer.requestMatchers(HttpMethod.DELETE,"/comment/**").hasAnyRole(ADMIN_ROLE, MODERATOR_ROLE);

                                                                    requestMatcherConfigurer.requestMatchers("/auth/**").permitAll();
                                                                    requestMatcherConfigurer.anyRequest().fullyAuthenticated(); //Only those who have logged in and have a token are allowed to do requests
                })
                .authenticationProvider(authenticationProvider);

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter(){
        return new JWTAuthenticationFilter();
    }


}
