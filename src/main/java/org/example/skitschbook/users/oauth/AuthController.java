package org.example.skitschbook.users.oauth;

import lombok.RequiredArgsConstructor;
import org.example.skitschbook.global.jwt.AuthTokens;
import org.example.skitschbook.users.oauth.naver.NaverLoginParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final OauthService oAuthService;

    @PostMapping("/naver")
    public ResponseEntity<AuthTokens> loginNaver(@RequestBody NaverLoginParams params) {
        return ResponseEntity.ok(oAuthService.login(params));
    }

}
