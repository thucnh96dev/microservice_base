package com.project.infra.token.opaque;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class OpaqueTokenService {

    public String createToken() {
        return RandomStringUtils.random(48, true, true);
    }
}
