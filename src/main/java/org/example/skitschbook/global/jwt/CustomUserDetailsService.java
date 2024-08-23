package org.example.skitschbook.global.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.skitschbook.users.UserRepository;
import org.example.skitschbook.users.Users;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Long userId = Long.valueOf(username);
        return usersRepository.findById(userId)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일의 회원이 없습니다."));
    }

    private UserDetails createUserDetails(Users users) {
        log.info("유저 디테일 설정");
        return User.builder()
                .username(users.getUserId().toString())
                .password("")
                .authorities(Collections.emptyList())
                .build();
    }

}
