package com.gyowanny.qima.products.configuration;

import com.gyowanny.qima.products.configuration.handler.CustomAuthenticationFailureHandler;
import com.gyowanny.qima.products.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  // Disable login
  //    http
//        .csrf(csrf -> csrf.disable())
//        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

  @Autowired
  private CustomUserDetailsService customUserDetailsService;
  @Autowired
  private CustomAuthenticationFailureHandler failureHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/login", "/", "/index.html", "/static/**", "/api/auth/**").permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .failureHandler(failureHandler)
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .permitAll()
        )
        .userDetailsService(customUserDetailsService);

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authBuilder = http.getSharedObject(
        AuthenticationManagerBuilder.class);
    authBuilder.userDetailsService(customUserDetailsService)
        .passwordEncoder(passwordEncoder());
    return authBuilder.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
