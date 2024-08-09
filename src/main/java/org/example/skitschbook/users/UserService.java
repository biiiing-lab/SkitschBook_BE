package org.example.skitschbook.users;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.example.skitschbook.global.dto.StatusResponse;
import org.example.skitschbook.global.jwt.JwtTokenProvider;
import org.example.skitschbook.users.dto.UserNicknameRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<StatusResponse> changeNickname(UserNicknameRequest nicknameRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 로그 추가 - 어떤 이메일을 찾고 있는지 확인
        System.out.println("이메일 : " + userDetails.getUsername());

        Users user = userRepository.findById(Long.valueOf(userDetails.getUsername())).orElseThrow();

        user.updateNickname(nicknameRequest.getNickname());

        return  ResponseEntity.ok(new StatusResponse(HttpStatus.OK.value(), "닉변 성공"));
    }

}
