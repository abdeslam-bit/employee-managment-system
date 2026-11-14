package com.myFullstack.employee_management.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // CURRENT: Injected custom JwtFilter to intercept incoming HTTP requests and validate JWT tokens
    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // Allow all auth endpoints (signup, login) without authentication
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/departments").permitAll()

                // ── Employees ─────────────────────────────────────────────────
                .requestMatchers(HttpMethod.GET, "/employees").hasRole("ADMIN")
              .requestMatchers(HttpMethod.GET, "/employees/{employeeId}").hasAnyRole("ADMIN", "USER")
              .requestMatchers(HttpMethod.POST, "/employees").hasAnyRole("ADMIN")
              .requestMatchers(HttpMethod.DELETE, "/employees/{employeeId}").hasAnyRole("ADMIN")
              .requestMatchers(HttpMethod.PUT, "/employees/{employeeId}").hasAnyRole("ADMIN", "USER")
              .requestMatchers(HttpMethod.POST, "/employees/{employeeId}/leave-requests").hasAnyRole("ADMIN", "USER")
              .requestMatchers(HttpMethod.GET, "/employees/{employeeId}/leave-requests").hasAnyRole("ADMIN", "USER")

                .anyRequest().authenticated()
            )
            // =========================================================================
            // DIFFERENCE FROM PREVIOUS CODE:
            //
            // PREVIOUS CODE:
            //   .httpBasic(Customizer.withDefaults());
            //   -> Relied on HTTP Basic authentication (credentials sent in every request header as base64).
            //   -> Spring Security managed sessions statefully by default.
            //
            // CURRENT CODE:
            //   1. SessionCreationPolicy.STATELESS:
            //      -> Spring Security will never create or use an HttpSession to store authentication state.
            //         Each request must carry its own JWT Bearer token.
            //   2. addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class):
            //      -> Registers JwtFilter right before UsernamePasswordAuthenticationFilter in the filter chain.
            //         On each request, JwtFilter extracts the "Bearer <token>" header, validates it,
            //         and populates SecurityContextHolder with the user authentication.
            // =========================================================================
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}

