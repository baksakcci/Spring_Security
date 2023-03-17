package com.example.springsecurityproject.config.oauth;

import java.util.Map;

public class GoogleOAuth2UserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes; // OAuth2User.getAttribute()의 return값. OAuth2 처리 후 결과를 이걸로 리턴해줌

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return "google_" + attributes.get("name");
    }
}
