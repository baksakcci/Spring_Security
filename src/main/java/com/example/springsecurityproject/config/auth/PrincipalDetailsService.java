package com.example.springsecurityproject.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.springsecurityproject.model.User;
import com.example.springsecurityproject.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// securityConfig(시큐리티 설정)에서 loginProcessUrl("/login")걸어놨기 때문에
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어있는 loadUserByUserName 함수가 실행
// 그렇게 흘러가게 설계해놈

@Service
@RequiredArgsConstructor
// 그냥 우리가 전에 썻던 Service라고 생각하면 될듯. UserDetailsService 구현체.
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // security session(내부 Authentication)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // form의 username이 여기에 인자로 오게되는거임
        User userEntity = userRepository.findByUsername(username);
        if(userEntity != null) {
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
     
}
