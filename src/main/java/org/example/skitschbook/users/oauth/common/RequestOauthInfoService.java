package org.example.skitschbook.users.oauth.common;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RequestOauthInfoService {

    private final Map<OauthProvider, OauthApiClient> clients;

    public RequestOauthInfoService(List<OauthApiClient> clients) {
        this.clients = clients.stream().collect(
                Collectors.toUnmodifiableMap(OauthApiClient::oauthProvider, Function.identity())
        );
    }

    public OauthInfoResponse request(OauthLoginParams params) {
        OauthApiClient client = clients.get(params.oauthProvider());
        String accessToken = client.requestAccessToken(params);
        return client.requestOauthInfo(accessToken);
    }
}
