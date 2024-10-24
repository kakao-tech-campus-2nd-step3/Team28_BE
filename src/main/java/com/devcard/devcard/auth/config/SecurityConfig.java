package com.devcard.devcard.auth.config;

import com.devcard.devcard.auth.service.OauthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private OauthService oauthService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    // 인증 없이 접근 가능한 경로 설정
                    auth.requestMatchers(
                            "/",
                            "/home",
                            "/login",
                            "/cards/**",
                            "/oauth2/**",
                            "/css/**",
                            "/js/**",
                            "/images/**",
                            "/h2-console/**"
                    ).permitAll();
                    // 그 외의 모든 요청은 인증 필요
                    auth.anyRequest().authenticated();
                })
                .oauth2Login(oauth -> oauth
                        .loginPage("/login") // 커스텀 로그인 페이지 경로 설정
                        .defaultSuccessUrl("/home") // 로그인 성공 후 이동할 경로
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(oauthService)
                        )
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/") // 로그아웃 성공 후 이동할 경로
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }
}
