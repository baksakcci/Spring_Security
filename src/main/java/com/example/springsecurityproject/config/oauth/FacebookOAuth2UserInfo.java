package com.example.springsecurityproject.config.oauth;

import java.util.Map;

public class FacebookOAuth2UserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    public FacebookOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "facebook";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return "facebook_" + attributes.get("name");
    }
}
