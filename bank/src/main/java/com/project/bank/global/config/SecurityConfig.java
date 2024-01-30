package com.project.bank.global.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@Configuration
public class SecurityConfig {


    //로그인과정 생략 : 개발자 직접 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        log.info("----- configure ------");



        http.formLogin().loginPage("/user/login") //사용자가 설정한 로그인 페이지
                .defaultSuccessUrl("/")//로그인 성공시 이동 경로
                .usernameParameter("memberId")//로그인시 사용될 유저 이름
                .passwordParameter("pswd")
                .failureUrl("/user/login/error")
                .and()//그리고
                .logout() //로그아웃
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))//로그아웃 경로
                .logoutSuccessUrl("/"); //로그아웃 성공시 이동 경로


        http.csrf().disable();

        http.formLogin().loginPage("/user/login");
        return http.build();
    }

    // 해시코드로 암호화 처리
    @Bean
    public PasswordEncoder passwordEncoder(){
        log.info("비밀번호 암호화");
        return new BCryptPasswordEncoder();
    }

    // 정적자원(resources -> static)들은 스프링시큐리티 적용에서 제외
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        log.info("--0--0-0-0-0-0-0-");

        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());

    }


}
