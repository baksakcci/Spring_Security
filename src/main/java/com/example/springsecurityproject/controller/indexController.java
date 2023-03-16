package com.example.springsecurityproject.controller;

import com.example.springsecurityproject.config.auth.PrincipalDetails;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.springsecurityproject.model.User;
import com.example.springsecurityproject.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Controller // View 반환
@RequiredArgsConstructor
public class indexController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication, @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("/test/login =================");
        System.out.println("userDetails : " + userDetails.getUsername());
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication : " + principal.getUsername());
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth) {
        System.out.println("/test/login =================");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        // 두 정보 다 똑같은것임. authentication interface로도, @AuthenticationPrincipal로도 가져올 수 있다.
        System.out.println("authentication : " + oAuth2User.getAttributes());
        System.out.println("oauth2User : " + oauth.getAttributes());
        return "세션 정보 확인하기";
    }

    @GetMapping("")
    public String index() {
        return "index"; // index라는 View를 반환
    }

    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDetails : " + principalDetails.getUsername());
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    // 시큐리티가 해당 주소 낚아챔
    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    // 회원가입을 하기 위해서는 비밀번호를 암호화하여 저장해야함(스프링 시큐리티가 그렇게 설계되어 있음)
    public String join(User user) {
        System.out.println(user);
        user.setRole("ROLE_USER");
        // password encrypt
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        System.out.println(encPassword);
        user.setPassword(encPassword);
        userRepository.save(user);
        return "redirect:/loginForm";
    }
    
    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    @ResponseBody
    public String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // 여러개 묶고 싶을때. hasRole 문법을 지원한다.
    // postAuthorize는 잘 사용안함
    @GetMapping("/data")
    @ResponseBody
    public String data() {
        return "데이터정보";
    }
}
