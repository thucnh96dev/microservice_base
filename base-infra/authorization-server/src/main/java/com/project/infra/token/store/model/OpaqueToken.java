package com.project.infra.token.store.model;

import org.springframework.security.authentication.BadCredentialsException;

import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@DiscriminatorValue("opaque")
public class OpaqueToken extends Token {

    @NotBlank
    @Size(max = 200)
    private String subject;

    @NotBlank
    @Size(max = 200)
    private String clientId;

    @NotBlank
    @Size(max = 200)
    private String issuer;

    @NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> scope;

    @NotNull
    private LocalDateTime issuedAt;

    @NotNull
    private LocalDateTime notBefore;

    @NotNull
    private boolean refreshToken;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Set<String> getScope() {
        return scope;
    }

    public void setScope(Set<String> scope) {
        this.scope = scope;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public LocalDateTime getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(LocalDateTime notBefore) {
        this.notBefore = notBefore;
    }

    public boolean isRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(boolean refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public boolean isReferenceToken() {
        return true;
    }

    public void validate() {
        if (LocalDateTime.now().isAfter(this.getExpiry())) {
            throw new BadCredentialsException("Expired");
        }
        if (LocalDateTime.now().isBefore(this.getNotBefore())) {
            throw new BadCredentialsException("Not yet valid");
        }
    }

    @Override
    public String toString() {
        return "OpaqueToken{" +
                "subject='" + subject + '\'' +
                ", clientId='" + clientId + '\'' +
                ", issuer='" + issuer + '\'' +
                ", scope='" + getScope() + '\'' +
                ", issuedAt=" + issuedAt +
                ", notBefore=" + notBefore +
                ", refreshToken=" + refreshToken +
                "} " + super.toString();
    }
}
