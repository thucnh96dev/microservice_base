package com.project.infra.token.store.dao;


import com.project.infra.token.store.model.JsonWebToken;

public interface JsonWebTokenRepository extends TokenRepository<JsonWebToken> {

    JsonWebToken findOneByValue(String value);

    JsonWebToken findOneByValueAndAccessToken(String value, boolean accessToken);
}
