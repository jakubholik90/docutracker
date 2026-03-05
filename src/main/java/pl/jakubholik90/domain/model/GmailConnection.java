package pl.jakubholik90.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class GmailConnection {

    private final Long id;
    private final Long userId;
    private final String email;
    private final String accessToken;
    private final String refreshToken;
    private final LocalDateTime tokenExpiry;
    private final LocalDateTime connectedAt;

    //getters
    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public LocalDateTime getTokenExpiry() {
        return tokenExpiry;
    }

    public LocalDateTime getConnectedAt() {
        return connectedAt;
    }
}
