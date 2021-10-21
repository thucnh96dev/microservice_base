package com.project.infra.jwks;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.project.infra.config.AuthorizationServerConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class JwtPki {

    private RSAKey publicKey;

    private JWKSet jwkSet;

    private JWSSigner signer;

    private JWSVerifier verifier;

    private final String issuer;

    public JwtPki(AuthorizationServerConfigurationProperties authorizationServerProperties) {
        this.issuer = authorizationServerProperties.getIssuer().toString();
    }

    @PostConstruct
    public void initPki() throws JOSEException {
        RSAKey rsaJWK = new RSAKeyGenerator(2048).keyID("1").generate();
        this.publicKey = rsaJWK.toPublicJWK();
        this.signer = new RSASSASigner(rsaJWK);
        this.jwkSet = new JWKSet(this.publicKey);
        this.verifier = new RSASSAVerifier(this.publicKey);
    }

    public JWSSigner getSigner() {
        return signer;
    }

    public JWSVerifier getVerifier() {
        return verifier;
    }

    public RSAKey getPublicKey() {
        return publicKey;
    }

    public String getIssuer() {
        return issuer;
    }

    public JWKSet getJwkSet() {
        return jwkSet;
    }
}
