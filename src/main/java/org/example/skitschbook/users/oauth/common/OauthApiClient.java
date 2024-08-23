package org.example.skitschbook.users.oauth.common;

public interface OauthApiClient {
    OauthProvider oauthProvider();
    String requestAccessToken(OauthLoginParams params);
    OauthInfoResponse requestOauthInfo(String accessToken);
}
