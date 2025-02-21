package com.example.demo.sercurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    private String[] admin_Employee_URL = {
            "/admin/**"
    };
    private String[] admin_Only_URL = {
            "/admin-only/**",
            "/account/**"
    };
    private String[] needAuth_URL = {
            "/shoping-cart/**", "/cart-status/**", "/profile"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeRequests(authorize -> authorize
                        .requestMatchers(admin_Employee_URL).hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers(admin_Only_URL).hasRole("ADMIN")
                        .requestMatchers(needAuth_URL).authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/user-login?unauth")
                        .loginProcessingUrl("/perform_login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .failureUrl("/user-login?error=true")
                        .defaultSuccessUrl("/home")
                        .permitAll()
                )

                .rememberMe(remember -> remember
                        .rememberMeParameter("rememberme")
                )
                .logout(logout -> logout
                        .logoutUrl("/user_logout")
                        .logoutSuccessUrl("/user-login")
                        .invalidateHttpSession(true)
                );

        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

}
