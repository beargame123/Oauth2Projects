package com.example.oauthprojects.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http)throws Exception{
        http
           .authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/admin/**").hasRole("ROLE_ADMIN")
                .anyRequest().permitAll()
           .and()
             .oauth2Login() // Oauth2기반의 로그인인 경우
                .loginPage("/loginForm") // 인증이 필요한 URL에 접근하면 설정한 URL로
                .defaultSuccessUrl("/")// 로그인 성공하면 설정한 URL로
                .failureUrl("/loginForm")// 로그인 실패 시 설정한 URL로
                .userInfoEndpoint()// 로그인 성공 후 사용자정보를 가져온다.
                .userService(prin);// 사용자정보를 처리할 때 사용한다.
    }
}
