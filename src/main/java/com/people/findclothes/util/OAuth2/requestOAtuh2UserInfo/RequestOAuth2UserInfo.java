package com.people.findclothes.util.OAuth2.requestOAtuh2UserInfo;

import org.springframework.util.MultiValueMap;

public interface RequestOAuth2UserInfo {
    String getTokenUrl();
    String getProfileUrl();
    MultiValueMap<String, String> getBody(String code);
}
