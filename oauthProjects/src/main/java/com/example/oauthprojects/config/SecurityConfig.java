package com.example.oauthprojects.config;

import com.example.oauthprojects.service.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalOauth2UserService principalOauth2UserService;

    @Override
    protected void configure(HttpSecurity http)throws Exception{
        http
           .authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/admin/**").hasRole("ROLE_ADMIN")
                .anyRequest().permitAll()
           .and()					//추가
                .formLogin()				// form기반의 로그인인 경우
                .loginPage("/loginForm")		// 인증이 필요한 URL에 접근하면 /loginForm으로 이동
                .usernameParameter("id")		// 로그인 시 form에서 가져올 값(id, email 등이 해당)
                .passwordParameter("pw")		// 로그인 시 form에서 가져올 값
                .loginProcessingUrl("/login")		// 로그인을 처리할 URL 입력
                .defaultSuccessUrl("/")			// 로그인 성공하면 "/" 으로 이동
                .failureUrl("/loginForm")		//로그인 실패 시 /loginForm으로 이동
           .and()
                .logout()					// logout할 경우
                .logoutUrl("/logout")			// 로그아웃을 처리할 URL 입력
                .logoutSuccessUrl("/")			// 로그아웃 성공 시 "/"으로 이동
           .and()
             .oauth2Login() // Oauth2기반의 로그인인 경우
                .loginPage("/loginForm") // 인증이 필요한 URL에 접근하면 설정한 URL로
                .defaultSuccessUrl("/")// 로그인 성공하면 설정한 URL로
                .failureUrl("/loginForm")// 로그인 실패 시 설정한 URL로
                .userInfoEndpoint()// 로그인 성공 후 사용자정보를 가져온다.
                .userService(principalOauth2UserService);// 사용자정보를 처리할 때 사용한다.
    }
}
