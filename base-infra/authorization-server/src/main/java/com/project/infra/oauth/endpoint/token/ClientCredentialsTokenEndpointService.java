package com.project.infra.oauth.endpoint.token;

import com.project.infra.config.AuthorizationServerConfigurationProperties;
import com.project.infra.oauth.client.model.AccessTokenFormat;
import com.project.infra.oauth.client.model.RegisteredClient;
import com.project.infra.oauth.common.ClientCredentials;
import com.project.infra.oauth.common.GrantType;
import com.project.infra.oauth.endpoint.token.resource.TokenRequest;
import com.project.infra.oauth.endpoint.token.resource.TokenResponse;
import com.project.infra.security.client.RegisteredClientAuthenticationService;
import com.project.infra.token.store.TokenService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.project.infra.oauth.endpoint.token.resource.TokenResponse.BEARER_TOKEN_TYPE;

@Service
public class ClientCredentialsTokenEndpointService {
    private static final Logger LOG =
            LoggerFactory.getLogger(ClientCredentialsTokenEndpointService.class);

    private final TokenService tokenService;
    private final AuthorizationServerConfigurationProperties authorizationServerProperties;
    private final RegisteredClientAuthenticationService registeredClientAuthenticationService;

    public ClientCredentialsTokenEndpointService(
            TokenService tokenService,
            AuthorizationServerConfigurationProperties authorizationServerProperties,
            RegisteredClientAuthenticationService registeredClientAuthenticationService) {
        this.tokenService = tokenService;
        this.authorizationServerProperties = authorizationServerProperties;
        this.registeredClientAuthenticationService = registeredClientAuthenticationService;
    }

    /* -------------------
    Access Token Request

    The client makes a request to the token endpoint by adding the
    following parameters using the "application/x-www-form-urlencoded"
    format per Appendix B with a character encoding of UTF-8 in the HTTP
    request entity-body:

    grant_type
          REQUIRED.  Value MUST be set to "client_credentials".

    scope
          OPTIONAL.  The scope of the access request as described by
          Section 3.3.

    The client MUST authenticate with the authorization server
    */
    public ResponseEntity<TokenResponse> getTokenResponseForClientCredentials(
            String authorizationHeader, TokenRequest tokenRequest) {

        LOG.debug("Exchange token for 'client credentials' with [{}]", tokenRequest);

        ClientCredentials clientCredentials =
                TokenEndpointHelper.retrieveClientCredentials(authorizationHeader, tokenRequest);

        if (clientCredentials == null) {
            return TokenEndpointHelper.reportInvalidClientError();
        }

        Duration accessTokenLifetime = authorizationServerProperties.getAccessToken().getLifetime();
        Duration refreshTokenLifetime = authorizationServerProperties.getRefreshToken().getLifetime();

        RegisteredClient registeredClient;

        try {
            registeredClient =
                    registeredClientAuthenticationService.authenticate(
                            clientCredentials.getClientId(), clientCredentials.getClientSecret());

        } catch (AuthenticationException ex) {
            return TokenEndpointHelper.reportInvalidClientError();
        }

        if (registeredClient.getGrantTypes().contains(GrantType.CLIENT_CREDENTIALS)) {

            Set<String> scopes = new HashSet<>();
            if (StringUtils.isNotBlank(tokenRequest.getScope())) {
                scopes = new HashSet<>(Arrays.asList(tokenRequest.getScope().split(" ")));
            }

            LOG.info(
                    "Creating token response for client credentials for client [{}]",
                    tokenRequest.getClient_id());
            return ResponseEntity.ok(
                    new TokenResponse(
                            AccessTokenFormat.JWT.equals(registeredClient.getAccessTokenFormat())
                                    ? tokenService
                                    .createAnonymousJwtAccessToken(
                                            clientCredentials.getClientId(), scopes, accessTokenLifetime)
                                    .getValue()
                                    : tokenService
                                    .createAnonymousOpaqueAccessToken(
                                            clientCredentials.getClientId(), scopes, accessTokenLifetime)
                                    .getValue(),
                            tokenService
                                    .createAnonymousRefreshToken(
                                            clientCredentials.getClientId(), scopes, refreshTokenLifetime)
                                    .getValue(),
                            accessTokenLifetime.toSeconds(),
                            null,
                            BEARER_TOKEN_TYPE));
        } else {
            return TokenEndpointHelper.reportUnauthorizedClientError();
        }
    }
}
