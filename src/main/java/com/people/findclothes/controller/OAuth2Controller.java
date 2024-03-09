package com.people.findclothes.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/login/oauth2")
public class OAuth2Controller {
    @GetMapping
    public void login(Authentication authentication) {
    }

    @GetMapping("/success")
    public String loginSuccess(Authentication authentication) {
        //oAuth2User.toString() 예시 : Name: [2346930276], Granted Authorities: [[USER]], User Attributes: [{id=2346930276, provider=kakao, name=김준우, email=bababoll@naver.com}]
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        //attributes.toString() 예시 : {id=2346930276, provider=kakao, name=김준우, email=bababoll@naver.com}
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return attributes.toString();
    }

    @GetMapping("/fail")
    public void loginFail() {
    }

    @GetMapping("/code/naver")
    public void naverLogin(@RequestParam("code") String code, OAuth2AuthenticationToken authentication) {
    }
}