package org.example.skitschbook.users.oauth.common;

public interface OauthInfoResponse {
    String getEmail();
    String getNickname();
    OauthProvider getOAuthProvider();
}
