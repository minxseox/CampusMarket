package com.example.CampusMarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/item/new", "/item/edit/**", "/item/delete/**").authenticated() // 인증 필요
                        .anyRequest().permitAll() // 나머지는 허용
                )
                .formLogin((formLogin) -> formLogin
                        .loginPage("/user/login")     // 커스텀 로그인 페이지
                        .loginProcessingUrl("/user/login") // Security가 가로채서 인증 처리
                        .defaultSuccessUrl("/")       // 로그인 성공 시 홈으로
                )
                .logout((logout) -> logout
                        .logoutUrl("/user/logout")    // 최신 문자열 매칭 방식
                        .logoutSuccessUrl("/")        // 로그아웃 시 홈으로
                        .invalidateHttpSession(true)  // 세션 무효화
                )
                // H2 콘솔 접근 허용을 위한 예외 처리 (개발 시 필요)
                .csrf((csrf) -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .headers((headers) -> headers.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)));

        return http.build();
    }
}