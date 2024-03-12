package com.people.findclothes.util.OAuth2.requestOAtuh2UserInfo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class RequestNaverInfo implements RequestOAuth2UserInfo {
    @Value("${spring.security.oauth2.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.naver.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.naver.authorization-grant-type}")
    private String grantType;

    @Value("${spring.security.oauth2.naver.state}")
    private String state;

    @Value("${spring.security.oauth2.naver.token-url}")
    private String tokenUrl;

    @Value("${spring.security.oauth2.naver.profile-url}")
    private String profileUrl;

    @Override
    public String getTokenUrl() {
        return tokenUrl;
    }

    @Override
    public String getProfileUrl() {
        return profileUrl;
    }

    @Override
    public MultiValueMap<String, String> getBody(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", grantType);
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("code", code);
        formData.add("state", state);
        return formData;
    }
}
