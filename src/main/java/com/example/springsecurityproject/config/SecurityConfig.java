package com.example.springsecurityproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.springsecurityproject.config.failure.LoginFailHandler;
import com.example.springsecurityproject.config.oauth.PrincipalOauth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secure 어노테이션 활성화, preAuthorize,postAuthorize 어노테이션 활성화
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalOauth2UserService principalOauth2UserService;

    // 해당 메서드의 리턴되는 객체를 IoC로 등록하게 해줌. 어디서든 이걸 쓸 수 있게 됨!!
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LoginFailHandler loginFailHandler() {
        return new LoginFailHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() // 인증만 되면 들어갈 수 있는 주소
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll() // 누구나 들어올 수 있다.
                .and()
                .formLogin()
                .loginPage("/loginForm")
                // http post로 받아온 body값 중에 form의 username을 .usernameParameter("username")으로 이름
                // 설정 가능
                .loginProcessingUrl("/login") // localhost:8080/login이 호출되면 security가 낚아채서 대신 로그인 진행
                // html 파일과 함께 보면 흐름 이해 가능. login 버튼을 누르는 것이라 할 수 있다.
                .defaultSuccessUrl("/") // defaulturl로 갈 수 있게 함. 만약 /user로 가서 로그인 하면 /user 그대로 다시 보여줌
                .and()// 여기서부터 oauth2 추가 부분
                .oauth2Login()
                .loginPage("/loginForm")
                .userInfoEndpoint()
                .userService(principalOauth2UserService)
                .and()
                .failureHandler(loginFailHandler());
    }

}
