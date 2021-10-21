package com.project.infra.oauth.endpoint.token;

import com.project.infra.config.AuthorizationServerConfigurationProperties;
import com.project.infra.oauth.client.model.AccessTokenFormat;
import com.project.infra.oauth.client.model.RegisteredClient;
import com.project.infra.oauth.common.ClientCredentials;
import com.project.infra.oauth.common.GrantType;
import com.project.infra.oauth.endpoint.token.resource.TokenRequest;
import com.project.infra.oauth.endpoint.token.resource.TokenResponse;
import com.project.infra.scim.model.ScimUserEntity;
import com.project.infra.scim.service.ScimService;
import com.project.infra.security.client.RegisteredClientAuthenticationService;
import com.project.infra.token.store.TokenService;
import com.project.infra.token.store.model.OpaqueToken;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
public class RefreshTokenEndpointService {
    private static final Logger LOG = LoggerFactory.getLogger(RefreshTokenEndpointService.class);

    private final TokenService tokenService;
    private final ScimService scimService;
    private final AuthorizationServerConfigurationProperties authorizationServerProperties;
    private final RegisteredClientAuthenticationService registeredClientAuthenticationService;

    public RefreshTokenEndpointService(
            TokenService tokenService,
            ScimService scimService, AuthorizationServerConfigurationProperties authorizationServerProperties,
            RegisteredClientAuthenticationService registeredClientAuthenticationService) {
        this.tokenService = tokenService;
        this.scimService = scimService;
        this.authorizationServerProperties = authorizationServerProperties;
        this.registeredClientAuthenticationService = registeredClientAuthenticationService;
    }

    /**
     * ------------------------- Refreshing an Access Token
     *
     * <p>If the authorization server issued a refresh token to the client, the client makes a refresh
     * request to the token endpoint by adding the following parameters using the
     * "application/x-www-form-urlencoded" format per Appendix B with a character encoding of UTF-8 in
     * the HTTP request entity-body:
     *
     * <p>grant_type REQUIRED. Value MUST be set to "refresh_token". refresh_token REQUIRED. The
     * refresh token issued to the client. scope OPTIONAL. The scope of the access request as
     * described by Section 3.3. The requested scope MUST NOT include any scope not originally granted
     * by the resource owner, and if omitted is treated as equal to the scope originally granted by
     * the resource owner.
     */
    public ResponseEntity<TokenResponse> getTokenResponseForRefreshToken(
            String authorizationHeader, TokenRequest tokenRequest) {

        LOG.debug("Exchange token for refresh token with [{}]", tokenRequest);

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

        if (registeredClient.getGrantTypes().contains(GrantType.REFRESH_TOKEN)) {
            OpaqueToken opaqueWebToken = tokenService.findOpaqueToken(tokenRequest.getRefresh_token());
            if (opaqueWebToken != null && opaqueWebToken.isRefreshToken()) {
                opaqueWebToken.validate();

                Set<String> scopes = new HashSet<>();
                if (StringUtils.isNotBlank(tokenRequest.getScope())) {
                    scopes = new HashSet<>(Arrays.asList(tokenRequest.getScope().split(" ")));
                }

                String subject = opaqueWebToken.getSubject();
                if (TokenService.ANONYMOUS_TOKEN.equals(subject)) {

                    LOG.info(
                            "Creating anonymous token response for refresh token with client [{}]",
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
                                    TokenResponse.BEARER_TOKEN_TYPE));
                } else {
                    Optional<ScimUserEntity> authenticatedUser =
                            scimService.findUserByIdentifier(UUID.fromString(opaqueWebToken.getSubject()));
                    if (authenticatedUser.isPresent()) {

                        LOG.info(
                                "Creating personalized token response for refresh token with client [{}]",
                                tokenRequest.getClient_id());

                        return ResponseEntity.ok(
                                new TokenResponse(
                                        AccessTokenFormat.JWT.equals(registeredClient.getAccessTokenFormat())
                                                ? tokenService
                                                .createPersonalizedJwtAccessToken(
                                                        authenticatedUser.get(),
                                                        clientCredentials.getClientId(),
                                                        null,
                                                        scopes,
                                                        accessTokenLifetime)
                                                .getValue()
                                                : tokenService
                                                .createPersonalizedOpaqueAccessToken(
                                                        authenticatedUser.get(),
                                                        clientCredentials.getClientId(),
                                                        scopes,
                                                        accessTokenLifetime)
                                                .getValue(),
                                        tokenService
                                                .createPersonalizedRefreshToken(
                                                        clientCredentials.getClientId(),
                                                        authenticatedUser.get(),
                                                        scopes,
                                                        refreshTokenLifetime)
                                                .getValue(),
                                        accessTokenLifetime.toSeconds(),
                                        null,
                                        TokenResponse.BEARER_TOKEN_TYPE));
                    }
                }
                tokenService.remove(opaqueWebToken);
            }
            return TokenEndpointHelper.reportInvalidClientError();
        } else {
            return TokenEndpointHelper.reportUnauthorizedClientError();
        }
    }
}
