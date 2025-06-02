package com.practice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class MyConfig {

    @Bean
    public UserDetailsService getUserDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(getUserDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                   .authenticationProvider(daoAuthenticationProvider())
                   .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // CSRF can be disabled if it's not needed
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER")
                .anyRequest().permitAll()  // Permit all other URLs
            )
            .formLogin(form -> form
                .loginPage("/signin")  // Specifies the custom login page
                .loginProcessingUrl("/dologin")  // The URL that will be processed for login
                .defaultSuccessUrl("/user/index", true)  // Redirect to /user/index after successful login
                .permitAll()  // Allows everyone to access the login page
            )
            .logout(logout -> logout
                .logoutUrl("/logout")  // Default logout URL (or use a custom one)
                .logoutSuccessUrl("/signin?logout")  // Redirect to login page after successful logout
                .invalidateHttpSession(true)  // Invalidate the session after logout
                .clearAuthentication(true)   // Clear authentication after logout
                .permitAll()  // Allow everyone to access the logout functionality
            );

        return http.build();
    }

}
