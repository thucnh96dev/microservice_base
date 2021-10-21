package com.project.infra.token.store.dao;


import com.project.infra.token.store.model.OpaqueToken;

public interface OpaqueTokenRepository extends TokenRepository<OpaqueToken> {

    OpaqueToken findOneByValue(String value);

    OpaqueToken findOneByValueAndRefreshToken(String value, boolean refreshToken);

}
