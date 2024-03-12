package com.people.findclothes.service;

import com.people.findclothes.domain.User;
import com.people.findclothes.domain.constant.UserRole;
import com.people.findclothes.dto.request.RequestUserLoginDto;
import com.people.findclothes.dto.request.RequestUserSaveDto;
import com.people.findclothes.dto.auth.CustomUserDetails;
import com.people.findclothes.dto.auth.OAuth2UserInfo;
import com.people.findclothes.exception.PasswordMismatchException;
import com.people.findclothes.exception.UserAlreadyExistsException;
import com.people.findclothes.exception.UserNotFoundException;
import com.people.findclothes.repository.UserRepository;
import com.people.findclothes.util.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * [일반 로그인]<br>
     * 프로필 정보를 통해 일치하는 유저 정보가 있다면 해당 유저 정보로 token 발급
     *
     * @param requestDto 로그인 시 입력한 id, password
     * @return 유저의 id 정보로 발급 받은 jwt
     * @exception UserNotFoundException 일치하는 유저 정보가 없는 경우
     * @exception PasswordMismatchException 일치하는 유저 정보가 존재하지만 비밀번호가 틀린 경우
     */
    @Transactional(readOnly = true)
    public String login(RequestUserLoginDto requestDto) {
        // 로그인 검사 : 유저 정보 존재 여부, 비밀번호 일치 여부
        User findUser = userRepository.findById(requestDto.getId()).orElseThrow(() -> new UserNotFoundException("해당 ID의 유저가 존재하지 않아 로그인 실패하였습니다."));
        if (!passwordEncoder.matches(requestDto.getPassword(), findUser.getPassword()))
            throw new PasswordMismatchException("비밀번호가 일치하지 않아 로그인 실패하였습니다.");

        // 토큰 생성
        String token = jwtTokenProvider.createToken(findUser.getId());

        // 레디스에 토큰 저장
        redisTemplate.opsForValue().set("JWT_TOKEN:" + requestDto.getId(), token);

        return token;
    }

    /**
     * [소셜 로그인]<br>
     * 프로필 정보를 통해 일치하는 유저 정보가 있다면 해당 유저 정보로 token 발급<br>
     * 일치하는 유저 정보가 없다면 회원가입 후 해당 유저 정보로 token 발급
     *
     * @param provider 소셜 로그인 인증 서버 (ex. google, kakao, naver)
     * @param userInfo 인증 서버에 요청한 프로필 정보
     * @return 유저의 id 정보로 발급 받은 jwt
     */
    @Transactional
    public String socialLogin(String provider, JSONObject userInfo) {
        // 로그인 처리 : 유저 정보가 존재한다면 해당 정보로 토큰 발급, 존재하지 않는다면 회원가입 후 해당 정보로 토큰 발급
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(provider, userInfo);
        User user = userRepository.findById(oAuth2UserInfo.getId())
                .orElseGet(() -> userRepository.save(oAuth2UserInfo.toEntity()));

        // 토큰 생성
        String token = jwtTokenProvider.createToken(user.getId());

        // 레디스에 토큰 저장
        redisTemplate.opsForValue().set("JWT_TOKEN:" + user.getId(), token);

        return token;
    }

    /**
     * [로그아웃]<br>
     * DB에서 로그인 유저의 id를 검색해 일치하는 jwt 삭제
     *
     */
    @Transactional(readOnly = true)
    public void logout() {
        // spring security 상 인증된 user detail의 username과 일치하는 jwt를 삭제
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        redisTemplate.delete("JWT_TOKEN:" + customUserDetails.getUsername());
    }

    /**
     * [회원 가입]
     *
     * @param requestDto 회원 가입 시 입력한 id, password, nickname, email
     * @exception UserAlreadyExistsException 입력 정보와 중복인 회원 정보가 있을 경우
     */
    @Transactional
    public void saveUser(RequestUserSaveDto requestDto) {
        if (isDuplicatedEmail(requestDto.getEmail()) || isDuplicatedId(requestDto.getId()) || isDuplicatedNickname(requestDto.getNickname())) {
            throw new UserAlreadyExistsException("중복된 회원 정보로 인해 회원가입에 실패하였습니다.");
        }

        userRepository.save(
                User.builder()
                        .id(requestDto.getId())
                        .email(requestDto.getEmail())
                        .nickname(requestDto.getNickname())
                        .userRole(UserRole.MEMBER)
                        .password(passwordEncoder.encode(requestDto.getPassword()))
                        .build());
    }

    /**
     * Unique한 값을 가져야하나, id가 중복된 값을 가질 경우를 검증
     *
     * @param id 회원 가입 시 입력한 값
     * @return boolean
     */
    public boolean isDuplicatedId(String id) {
        return userRepository.findById(id).isPresent();
    }

    /**
     * Unique한 값을 가져야하나, email이 중복된 값을 가질 경우를 검증
     *
     * @param email 회원 가입 시 입력한 값
     * @return boolean
     */
    public boolean isDuplicatedEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * Unique한 값을 가져야하나, nickname이 중복된 값을 가질 경우를 검증
     *
     * @param nickname 회원 가입 시 입력한 값
     * @return boolean
     */
    public boolean isDuplicatedNickname(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }
}