package org.example.skitschbook.users.oauth;

public interface OauthApiClient {
    OauthProvider oauthProvider();
    String requestAccessToken(OauthLoginParams params);
    OauthInfoResponse requestOauthInfo(String accessToken);
}
