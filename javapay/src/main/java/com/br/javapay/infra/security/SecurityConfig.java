package com.br.javapay.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/transferencias/**").permitAll()
                        .requestMatchers("/app/**").permitAll()
                        // Legacy entry points (redirected to /app/*) - allow for convenience while developing
                        .requestMatchers("/", "/login", "/register", "/dashboard", "/usuarios", "/usuarios/**", "/contas", "/contas/**", "/403", "/404").permitAll()
                        // Static resources
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}
