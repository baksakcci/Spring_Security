package com.example.springsecurityproject.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @GetMapping("")
    public String index() {
        return "index"; // index라는 View를 반환
    }

    @GetMapping("/user")
    public @ResponseBody String user() {
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
