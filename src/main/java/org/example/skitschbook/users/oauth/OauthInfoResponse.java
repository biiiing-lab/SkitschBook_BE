package org.example.skitschbook.users.oauth;

public interface OauthInfoResponse {
    String getEmail();
    String getNickname();
    OauthProvider getOAuthProvider();
}
