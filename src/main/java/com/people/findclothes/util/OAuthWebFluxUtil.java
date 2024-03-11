package com.people.findclothes.util;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OAuthWebFluxUtil {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String KAKAO_GRANT_TYPE;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URI;

    /**
     * OAuth Kakao - 인가 코드를 통해서 토큰을 요청한다.
     *
     * @param code 인증 서버에서 로그인에 성공하여 받은 인가 코드
     * @return JSONObject - token_type, access_token, id_token, expires_in, refresh_token, refresh_token_expires_in, scope
     */
    public JSONObject requestToken(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", KAKAO_GRANT_TYPE);
        formData.add("client_id", KAKAO_CLIENT_ID);
        formData.add("redirect_uri", KAKAO_REDIRECT_URI);
        formData.add("client_secret", KAKAO_CLIENT_SECRET);
        formData.add("code", code);

        return WebClient.builder()
                .baseUrl("https://kauth.kakao.com/oauth/token")
                .build()
                .post()
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(JSONObject.class)
                .block();
    }

    /**
     * OAuth Kakao - 액세스 토큰를 통해서 사용자 정보를 요청한다.
     *
     * @param accessToken 인증 서버에서 인가 코드를 통해 발급 받은 액세스 토큰
     * @return JSONObject - id, connected_at, properties, kakao_account
     */
    public JSONObject requestUserInfo(String accessToken) {
        return WebClient.builder()
                .baseUrl("https://kapi.kakao.com/v2/user/me")
                .build()
                .post()
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(JSONObject.class)
                .block();
    }
}
