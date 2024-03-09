package com.people.findclothes.service;

import com.people.findclothes.domain.User;
import com.people.findclothes.dto.security.CustomUserDetails;
import com.people.findclothes.oauth2.GoogleUserInfo;
import com.people.findclothes.oauth2.KakaoUserInfo;
import com.people.findclothes.oauth2.NaverUserInfo;
import com.people.findclothes.oauth2.OAuth2UserInfo;
import com.people.findclothes.repository.UserRepository;
import com.people.findclothes.domain.constant.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        // DefaultOAuth2UserService 객체에서 OAuth 서비스(kakao, google, naver)에서 가져온 유저 정보 load
//        System.out.println("oAuth2User");
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//
//        // 클라이언트 등록 ID(google, naver, kakao)와 사용자 이름 속성
//        String registrationId = userRequest.getClientRegistration().getRegistrationId();
//        String userNameAttributeName = userRequest.getClientRegistration()
//                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
//        System.out.println("registrationId = " + registrationId);
//        log.info("registrationId = {}", registrationId);
//        log.info("userNameAttributeName = {}", userNameAttributeName);
//
//        // OAuth2User 정보로 OAuth2Attribute 객체 생성
//        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
//
//        //  OAuth2Attribute의 속성값들을 Map으로 반환
//        Map<String, Object> memberAttribute = oAuth2Attribute.convertToMap();
//        log.info("memberAttribute = {}", memberAttribute);
////
////        // 회원 조회
////        Optional<User> findMember = userRepository.findById((String) memberAttribute.get("id"));
////        if(findMember.isEmpty()) {
////            // 회원이 존재하지 않을경우, memberAttribute의 exist 값을 false로 넣어준다.
////            memberAttribute.put("exist", false);
////        }
//
//        memberAttribute.put("exist", true);
//        return new DefaultOAuth2User(
//                Collections.singleton(new SimpleGrantedAuthority("MEMBER")),
//                memberAttribute, "");
//    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("getAttributes : {}" + oAuth2User.getAttributes());
        log.info("getAttributes : {}", oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = null;
        switch (provider) {
            case "google":
                log.info("구글 로그인 요청");
                oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
                break;
            case "kakao":
                log.info("카카오 로그인 요청");
                oAuth2UserInfo = new KakaoUserInfo((Map) oAuth2User.getAttributes());
                break;
            case "naver":
                log.info("네이버 로그인 요청");
                oAuth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
                break;
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String loginId = provider + "_" + providerId;
        String nickname = oAuth2UserInfo.getNickname();


        Optional<User> optionalUser = userRepository.findById(loginId);
        User user = null;

        if (optionalUser.isEmpty()) {
            user = User.builder()
                    .id(loginId)
                    .email(email)
                    .nickname(nickname)
                    .provider(provider)
                    .userRole(UserRole.MEMBER)
                    .build();
            userRepository.save(user);
        } else {
            user = optionalUser.get();
        }

        return new CustomUserDetails(user, oAuth2User.getAttributes());
    }
}

// TODO : email을 받는 구글과 같은 경우 해당 이메일로 이미 가입된 회원이 있는지 확인 과정?