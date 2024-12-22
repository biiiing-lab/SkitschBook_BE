package org.example.skitschbook.users.oauth.google;

import lombok.Getter;
import lombok.Setter;
import org.example.skitschbook.users.oauth.common.OauthInfoResponse;
import org.example.skitschbook.users.oauth.common.OauthProvider;

@Getter
@Setter
public class GoogleInfoResponse implements OauthInfoResponse {

    private String email;
    private String name;

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return name;
    }

    @Override
    public OauthProvider getOAuthProvider() {
        return OauthProvider.GOOGLE;
    }
}