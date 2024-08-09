package org.example.skitschbook.users.oauth;

import lombok.RequiredArgsConstructor;
import org.example.skitschbook.global.jwt.AuthTokens;
import org.example.skitschbook.global.jwt.AuthTokensGenerator;
import org.example.skitschbook.global.jwt.JwtTokenProvider;
import org.example.skitschbook.users.UserRepository;
import org.example.skitschbook.users.Users;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {
    private final UserRepository userRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOauthInfoService requestOauthInfoService;

    public AuthTokens login(OauthLoginParams params) {
        OauthInfoResponse oAuthInfoResponse = requestOauthInfoService.request(params);
        Long memberId = findOrCreateMember(oAuthInfoResponse);
        return authTokensGenerator.generate(memberId);
    }

    private Long findOrCreateMember(OauthInfoResponse oAuthInfoResponse) {
        return userRepository.findByEmail(oAuthInfoResponse.getEmail())
                .map(Users::getUserId)
                .orElseGet(() -> newMember(oAuthInfoResponse));
    }

    private Long newMember(OauthInfoResponse oAuthInfoResponse) {
        Users users = Users.builder()
                .email(oAuthInfoResponse.getEmail())
                .username(oAuthInfoResponse.getNickname())
                .provider(oAuthInfoResponse.getOAuthProvider())
                .build();

        return userRepository.save(users).getUserId();
    }

}
