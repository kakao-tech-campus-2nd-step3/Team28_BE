package com.devcard.devcard.auth.config;

import com.devcard.devcard.auth.service.OauthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private OauthService oauthService;
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth->{
                    auth.requestMatchers("/", "/h2-console/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                .oauth2Login(oauth -> oauth
                        .defaultSuccessUrl("/success")
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(oauthService)));

        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }
}
