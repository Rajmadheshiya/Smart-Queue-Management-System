package com.raj.smartqueue.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/patient/join",
                    "/patient/status",
                    "/css/**",
                    "/admin/login"
                ).permitAll()

                .requestMatchers(
                    "/",
                    "/patient/form",
                    "/patient/list",
                    "/patient/edit/**",
                    "/patient/delete/**",
                    "/patient/complete/**",
                    "/patient/next"
                ).hasRole("ADMIN")

                .anyRequest().permitAll()
            )

            .formLogin(form -> form
                .loginPage("/admin/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )

            .logout(logout -> logout
                .logoutSuccessUrl("/admin/login?logout")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager userDetailsManager(PasswordEncoder passwordEncoder) {

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("Admin@12345"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }
}