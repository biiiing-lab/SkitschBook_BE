package org.example.skitschbook.users.oauth.common;

import org.springframework.util.MultiValueMap;

public interface OauthLoginParams {
    OauthProvider oauthProvider();
    MultiValueMap<String, String> makeBody();
}
