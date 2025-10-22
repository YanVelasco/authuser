package com.ead.authuser.configs.security;


import com.ead.authuser.configs.security.jwt.AuthenticationJwtFilter;
import com.ead.authuser.configs.security.jwt.JwtProvider;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/auth/**",
    };

    final UserDetailsServiceImpl userDetailsServiceImpl;
    final AuthenticationEntryPointImpl authenticationEntryPoint;
    final JwtProvider jwtProvider;
    final AccessDeniedHandler accessDeniedHandler;

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl,
                             AuthenticationEntryPointImpl authenticationEntryPoint, JwtProvider jwtProvider, AccessDeniedHandler accessDeniedHandler) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtProvider = jwtProvider;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public AuthenticationJwtFilter authenticationJwtFilter() {
        return new AuthenticationJwtFilter(jwtProvider, userDetailsServiceImpl);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint(authenticationEntryPoint)
                                .accessDeniedHandler(accessDeniedHandler)
                )
                .authorizeHttpRequests(authorize ->
                                authorize
                                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                                        .requestMatchers(AUTH_WHITELIST).permitAll()
//                                .requestMatchers(HttpMethod.GET, "/users/**").hasAnyRole("ADMIN")
                                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        http.addFilterBefore(authenticationJwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
