package com.project.infra.oauth.endpoint.introspection;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.project.infra.oauth.common.AuthenticationUtil;
import com.project.infra.oauth.common.ClientCredentials;
import com.project.infra.oauth.endpoint.introspection.resource.IntrospectionRequest;
import com.project.infra.oauth.endpoint.introspection.resource.IntrospectionResponse;
import com.project.infra.scim.model.ScimUserEntity;
import com.project.infra.scim.service.ScimService;
import com.project.infra.token.jwt.JsonWebTokenService;
import com.project.infra.token.store.TokenService;
import com.project.infra.token.store.model.JsonWebToken;
import com.project.infra.token.store.model.OpaqueToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
@RestController
@RequestMapping(IntrospectionEndpoint.ENDPOINT)
public class IntrospectionEndpoint {

    public static final String ENDPOINT = "/introspect";

    private final TokenService tokenService;
    private final ScimService scimService;
    private final JsonWebTokenService jsonWebTokenService;

    public IntrospectionEndpoint(
            TokenService tokenService, ScimService scimService, JsonWebTokenService jsonWebTokenService) {
        this.tokenService = tokenService;
        this.scimService = scimService;
        this.jsonWebTokenService = jsonWebTokenService;
    }

    @PostMapping
    public ResponseEntity<IntrospectionResponse> introspect(
            @RequestHeader("Authorization") String authorizationHeader,
            @ModelAttribute("introspection_request") IntrospectionRequest introspectionRequest) {

        ClientCredentials clientCredentials;

        try {

            clientCredentials = AuthenticationUtil.fromBasicAuthHeader(authorizationHeader);
            if (clientCredentials == null) {
                return reportInvalidClientError();
            }

            String tokenValue = introspectionRequest.getToken();

            if (tokenValue == null || tokenValue.isBlank()) {
                return ResponseEntity.ok(new IntrospectionResponse(false));
            }

            JsonWebToken jsonWebToken = tokenService.findJsonWebToken(tokenValue);
            if (jsonWebToken != null) {
                return ResponseEntity.ok(getIntrospectionResponse(jsonWebToken));
            } else {
                OpaqueToken opaqueWebToken = tokenService.findOpaqueToken(tokenValue);
                if (opaqueWebToken != null) {
                    return ResponseEntity.ok(getIntrospectionResponse(opaqueWebToken));
                } else {
                    return ResponseEntity.ok(new IntrospectionResponse(false));
                }
            }
        } catch (BadCredentialsException ex) {
            return reportInvalidClientError();
        }
    }

    private IntrospectionResponse getIntrospectionResponse(OpaqueToken opaqueWebToken) {
        String clientId;
        Optional<ScimUserEntity> user;
        try {
            opaqueWebToken.validate();
            clientId = opaqueWebToken.getClientId();
            if (TokenService.ANONYMOUS_TOKEN.equals(opaqueWebToken.getSubject())) {
                IntrospectionResponse introspectionResponse = new IntrospectionResponse();
                introspectionResponse.setActive(true);
                introspectionResponse.setClient_id(clientId);
                introspectionResponse.setSub(TokenService.ANONYMOUS_TOKEN);
                introspectionResponse.setUsername(TokenService.ANONYMOUS_TOKEN);
                introspectionResponse.setExp(opaqueWebToken.getExpiry().atZone(ZoneId.systemDefault()).toEpochSecond());
                introspectionResponse.setIss(opaqueWebToken.getIssuer());
                introspectionResponse.setNbf(opaqueWebToken.getNotBefore().atZone(ZoneId.systemDefault()).toEpochSecond());
                introspectionResponse.setIat(opaqueWebToken.getIssuedAt().atZone(ZoneId.systemDefault()).toEpochSecond());
                return introspectionResponse;
            } else {
                user = scimService.findUserByIdentifier(UUID.fromString(opaqueWebToken.getSubject()));
                return user.map(
                                u -> {
                                    IntrospectionResponse introspectionResponse = new IntrospectionResponse();
                                    introspectionResponse.setActive(true);
                                    introspectionResponse.setClient_id(clientId);
                                    introspectionResponse.setSub(u.getIdentifier().toString());
                                    introspectionResponse.setUsername(u.getUserName());
                                    introspectionResponse.setExp(opaqueWebToken.getExpiry().atZone(ZoneId.systemDefault()).toEpochSecond());
                                    introspectionResponse.setIss(opaqueWebToken.getIssuer());
                                    introspectionResponse.setNbf(opaqueWebToken.getNotBefore().atZone(ZoneId.systemDefault()).toEpochSecond());
                                    introspectionResponse.setIat(opaqueWebToken.getIssuedAt().atZone(ZoneId.systemDefault()).toEpochSecond());
                                    return introspectionResponse;
                                })
                        .orElse(new IntrospectionResponse(false));
            }
        } catch (BadCredentialsException ex) {
            return new IntrospectionResponse(false);
        }
    }

    private IntrospectionResponse getIntrospectionResponse(JsonWebToken jsonWebToken) {
        String clientId;
        String ctx;

        try {
            JWTClaimsSet jwtClaimsSet =
                    jsonWebTokenService.parseAndValidateToken(jsonWebToken.getValue());
            clientId = jwtClaimsSet.getStringClaim("client_id");
            ctx = jwtClaimsSet.getStringClaim("ctx");
            String subject = jwtClaimsSet.getSubject();

            if (TokenService.ANONYMOUS_TOKEN.equals(ctx)) {
                IntrospectionResponse introspectionResponse = new IntrospectionResponse();
                introspectionResponse.setActive(true);
                introspectionResponse.setClient_id(clientId);
                introspectionResponse.setSub(clientId);
                introspectionResponse.setUsername(clientId);
                introspectionResponse.setIss(jwtClaimsSet.getIssuer());
                introspectionResponse.setNbf(jwtClaimsSet.getNotBeforeTime().getTime());
                introspectionResponse.setIat(jwtClaimsSet.getIssueTime().getTime());
                introspectionResponse.setExp(jwtClaimsSet.getExpirationTime().getTime());
                return introspectionResponse;
            } else {
                Optional<ScimUserEntity> user = scimService.findUserByIdentifier(UUID.fromString(subject));
                return user.map(
                                u -> {
                                    IntrospectionResponse introspectionResponse = new IntrospectionResponse();
                                    introspectionResponse.setActive(true);
                                    introspectionResponse.setClient_id(clientId);
                                    introspectionResponse.setSub(u.getIdentifier().toString());
                                    introspectionResponse.setUsername(u.getUserName());
                                    introspectionResponse.setIss(jwtClaimsSet.getIssuer());
                                    introspectionResponse.setNbf(jwtClaimsSet.getNotBeforeTime().getTime());
                                    introspectionResponse.setIat(jwtClaimsSet.getIssueTime().getTime());
                                    introspectionResponse.setExp(jwtClaimsSet.getExpirationTime().getTime());
                                    return introspectionResponse;
                                })
                        .orElse(new IntrospectionResponse(false));
            }
        } catch (ParseException | JOSEException e) {
            return new IntrospectionResponse(false);
        }
    }

    private ResponseEntity<IntrospectionResponse> reportInvalidClientError() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header("WWW-Authenticate", "Basic")
                .body(new IntrospectionResponse("invalid_client"));
    }
}
