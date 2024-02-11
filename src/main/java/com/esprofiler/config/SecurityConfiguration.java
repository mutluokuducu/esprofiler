package com.esprofiler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers("/unauthenticated")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .oauth2Login(
            oauth2Log ->
                oauth2Log.defaultSuccessUrl("http://localhost:9994/login/oauth2/code/cognito"))
        .logout(
            logout ->
                logout
                    .logoutUrl("/logout") // Specifies the logout URL
                    .logoutSuccessUrl(
                        "/login?logout") // Specifies the URL to redirect to after logout
                    .invalidateHttpSession(true) // Invalidates the HTTP session
                    .deleteCookies("JSESSIONID") // Deletes the JSESSIONID cookie
                    .clearAuthentication(true) // Clears the authentication
            );

    return http.build();
  }
}
