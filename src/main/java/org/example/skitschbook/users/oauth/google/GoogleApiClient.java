package org.example.skitschbook.users.oauth.google;

import org.example.skitschbook.users.oauth.common.OauthApiClient;
import org.example.skitschbook.users.oauth.common.OauthInfoResponse;
import org.example.skitschbook.users.oauth.common.OauthLoginParams;
import org.example.skitschbook.users.oauth.common.OauthProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleApiClient implements OauthApiClient {

    private final RestTemplate restTemplate;

    public GoogleApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public OauthProvider oauthProvider() {
        return OauthProvider.GOOGLE;
    }

    @Override
    public String requestAccessToken(OauthLoginParams params) {
        GoogleLoginParams googleParams = (GoogleLoginParams) params;

        MultiValueMap<String, String> body = googleParams.makeBody();
        String url = "https://oauth2.googleapis.com/token";

        GoogleTokens tokens = restTemplate.postForObject(url, body, GoogleTokens.class);
        if (tokens == null || tokens.getAccessToken() == null) {
            throw new IllegalStateException("Failed to retrieve access token from Google");
        }
        return tokens.getAccessToken();
    }

    @Override
    public OauthInfoResponse requestOauthInfo(String accessToken) {
        String url = "https://www.googleapis.com/oauth2/v2/userinfo";

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer " + accessToken);

        GoogleInfoResponse response = restTemplate.getForObject(url, GoogleInfoResponse.class);
        if (response == null) {
            throw new IllegalStateException("Failed to retrieve user info from Google");
        }
        return response;
    }
}