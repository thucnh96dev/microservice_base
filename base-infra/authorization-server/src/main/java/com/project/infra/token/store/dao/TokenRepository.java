package com.project.infra.token.store.dao;

import com.project.infra.token.store.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository<T extends Token> extends JpaRepository<T, Long> {
}
