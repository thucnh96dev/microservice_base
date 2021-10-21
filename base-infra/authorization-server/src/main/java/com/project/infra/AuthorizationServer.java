package com.project.infra;

import com.project.infra.config.AuthorizationServerConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * @author : thucnh
 * @mailto : thucnh96.dev@gmail.com
 * @created :07/10/2021 - 4:06 PM
 */
@EnableAuthorizationServer
@SpringBootApplication
@EnableConfigurationProperties(AuthorizationServerConfigurationProperties.class)
public class AuthorizationServer {
    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServer.class, args);
    }
}
