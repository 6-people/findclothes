package com.people.findclothes.util.OAuth2.requestOAtuh2UserInfo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class RequestKakaoInfo implements RequestOAuth2UserInfo {
    @Value("${spring.security.oauth2.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.kakao.authorization-grant-type}")
    private String grantType;

    @Value("${spring.security.oauth2.kakao.redirect-url}")
    private String redirectUrl;

    @Value("${spring.security.oauth2.kakao.token-url}")
    private String tokenUrl;

    @Value("${spring.security.oauth2.kakao.profile-url}")
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
        formData.add("redirect_uri", redirectUrl);
        formData.add("code", code);
        return formData;
    }
}
