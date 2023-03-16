package com.example.springsecurityproject.config.oauth;

import com.example.springsecurityproject.config.auth.PrincipalDetails;
import com.example.springsecurityproject.model.User;
import com.example.springsecurityproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

// 구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수

/*
 DB에 저장할 User Data는?
 DB에 저장되는 User값과 비교해서 로그인 할 목적으로 쓰이는것은 아님
 이미 Oauth방식으로 구글이 로그인 시켜주기 때문
 그래서 여기는 그냥 이메일 등 데이터 저장용.
 그냥 강제 회원가입이라 생각하면 됨
 */
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println(userRequest.getClientRegistration()); // registrationId로 어떤 OAuth로 로그인했는지 확인 가능
        System.out.println(userRequest.getAccessToken().getTokenValue()); // access token 값
        // userRequest정보 -> loadUser함수를 통해 구글로부터 회원 프로필을 받을 수 있다.(.getAttributes())
        System.out.println(super.loadUser(userRequest).getAttributes()); // userRequest 데이터 출력
        // 강제 회원가입 진행
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String client = userRequest.getClientRegistration().getClientName();
        String clientId = oAuth2User.getAttribute("sub");
        String username = client + "_" + clientId; // google_1912394576278..
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);
        if(userEntity == null) {
            userEntity = new User(username, "opar13!", email, role, client, clientId);
            userRepository.save(userEntity);
        }
        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
