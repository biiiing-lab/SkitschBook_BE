package org.example.skitschbook.users.oauth.naver;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.skitschbook.users.oauth.OauthLoginParams;
import org.example.skitschbook.users.oauth.OauthProvider;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@NoArgsConstructor
public class NaverLoginParams implements OauthLoginParams {
    private String authorizationCode;
    private String state;

    @Override
    public OauthProvider oauthProvider() {
        return OauthProvider.NAVER;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("state", state);
        return body;
    }
}
