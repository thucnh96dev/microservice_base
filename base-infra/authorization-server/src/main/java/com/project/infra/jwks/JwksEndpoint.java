package com.project.infra.jwks;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
@RestController
@RequestMapping(JwksEndpoint.ENDPOINT)
public class JwksEndpoint {

    public static final String ENDPOINT = "/jwks";

    private final JwtPki jwtPki;

    public JwksEndpoint(JwtPki jwtPki) {
        this.jwtPki = jwtPki;
    }

    @GetMapping
    public Map<String, Object> jwksEndpoint() {
        return jwtPki.getJwkSet().toJSONObject();
    }
}
