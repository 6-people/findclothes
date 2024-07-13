package com.people.findclothes.util.OAuth2;

import com.people.findclothes.util.OAuth2.requestOAtuh2UserInfo.RequestNaverInfo;
import com.people.findclothes.util.OAuth2.requestOAtuh2UserInfo.RequestOAuth2UserInfo;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OAuthRequestFactory {

    public JSONObject getOAuth2UserInfo(RequestOAuth2UserInfo requestUserInfo, String code) {
        return requestUserInfo(code, requestUserInfo.getProfileUrl());
    }

    public JSONObject requestToken(RequestNaverInfo info, String code) {
        return WebClient.builder()
                .baseUrl(info.getTokenUrl() + "?grant_type=authorization_code&client_id=" + info.getClientId() + "&client_secret=" + info.getClientSecret() + "&state=" + info.getState() + "&code=" + code)
                .build()
                .get()
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(JSONObject.class)
                .block();
    }

    public JSONObject requestUserInfo(String accessToken, String profileUrl) {
        return WebClient.builder()
                .baseUrl(profileUrl)
                .build()
                .post()
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(JSONObject.class)
                .block();
    }

}
