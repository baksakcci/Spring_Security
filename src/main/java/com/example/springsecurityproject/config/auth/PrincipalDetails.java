package com.example.springsecurityproject.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.springsecurityproject.model.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

// 시큐리티가 /login을 낚아채서 로그인을 진행
// 로그인 진행이 완료되면 session을 만들어줌
// 시큐리티 자신만의 session가 있다. (Security ContextHolder)
// 저 안에 들어갈 수 있는 객체가 정해져있다. -> Authentication 객체
// 저 안에는 User 정보가 있어야 한다.
// 이것도 객체가 정해져 있다. -> UserDetail(인터페이스) 객체 타입 -> 얘를 꺼내면 User가 나온다.

// Security Session (Security ContextHolder) -> Authentication(인증) -> UserDetails(PrincipalDetails) -> User!

public class PrincipalDetails implements UserDetails, OAuth2User {
    // composition
    private User user;

    private Map<String, Object> attributes;

    public PrincipalDetails(User user) {
        this.user = user; // 밖에서 User를 만들어서 주입할 예정
    }

    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    // 해당 User의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            public String getAuthority() {
                return user.getRole();
            };
        });
        return collect;
    }

    // 패스워드
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // 유저이름
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 계정이 만료되었는가
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠길건가
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 기간이 지났는데도 사용하는것은 아니니
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정을 활성화할건가
    // 1년동안 회원이 로그인을 안한다면 휴먼 계정을 하기로 함 -> 현재시간 - 로그인시간 = 1년 이상 차이날 때
    @Override
    public boolean isEnabled() {
        return true;
    }

    /*
    oauth2user method override
     */
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
}
