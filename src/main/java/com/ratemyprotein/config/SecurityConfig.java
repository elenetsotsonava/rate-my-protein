package com.ratemyprotein.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .authorizeHttpRequests(authorize -> authorize

                        // Public pages
                        .requestMatchers(
                                "/",
                                "/products",
                                "/products/**",
                                "/register",
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/error"
                        ).permitAll()

                        // Administrator pages
                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")

                        // Logged-in user pages
                        .requestMatchers(
                                "/reviews/**",
                                "/profile/**"
                        ).authenticated()

                        // Everything else requires login
                        .anyRequest()
                        .authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/products", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}