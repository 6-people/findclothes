package com.people.findclothes.service;

import com.people.findclothes.domain.User;
import com.people.findclothes.dto.auth.CustomUserDetails;
import com.people.findclothes.exception.UserNotFoundException;
import com.people.findclothes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustumUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("해당 ID의 유저가 존재하지 않아 로그인 실패하였습니다."));
        return new CustomUserDetails(user);
    }
}