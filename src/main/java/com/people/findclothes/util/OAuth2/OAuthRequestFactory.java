package com.people.findclothes.util.OAuth2;

import com.people.findclothes.util.OAuth2.requestOAtuh2UserInfo.RequestOAuth2UserInfo;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OAuthRequestFactory {

    public JSONObject getOAuth2UserInfo(RequestOAuth2UserInfo requestUserInfo, String code) {
        JSONObject tokenInfo = requestToken(requestUserInfo.getTokenUrl(), requestUserInfo.getBody(code));
        return requestUserInfo(String.valueOf(tokenInfo.get("access_token")), requestUserInfo.getProfileUrl());
    }

    public JSONObject requestToken(String tokenUrl, MultiValueMap<String, String> formData) {
        return WebClient.builder()
                .baseUrl(tokenUrl)
                .build()
                .post()
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .body(BodyInserters.fromFormData(formData))
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
