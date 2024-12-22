package org.example.skitschbook.users.oauth.google;


import org.example.skitschbook.users.oauth.common.OauthLoginParams;
import org.example.skitschbook.users.oauth.common.OauthProvider;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class GoogleLoginParams implements OauthLoginParams {

    private final String clientId;
    private final String clientSecret;
    private final String code;
    private final String redirectUri;

    public GoogleLoginParams(String clientId, String clientSecret, String code, String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.code = code;
        this.redirectUri = redirectUri;
    }

    @Override
    public OauthProvider oauthProvider() {
        return OauthProvider.GOOGLE;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("code", code);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");
        return body;
    }
}