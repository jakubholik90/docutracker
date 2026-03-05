package pl.jakubholik90.adapter.out.persistence.jpa.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "gmail_connections")
public class GmailConnectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, name = "user_id")
    private Long userId;

    @Column(nullable = false, name="email")
    private String email;

    @Column(nullable = false, length = 2048, name="access_token")
    private String accessToken;

    @Column(nullable = false, length = 2048, name="refresh_token")
    private String refreshToken;

    @Column(nullable = false, name="token_expiry")
    private LocalDateTime tokenExpiry;

    @Column(nullable = false, name="connected_at")
    private LocalDateTime connectedAt;

    // constructors
    public GmailConnectionEntity() {
    }

    // getters
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

    //setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setTokenExpiry(LocalDateTime tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
    }

    public void setConnectedAt(LocalDateTime connectedAt) {
        this.connectedAt = connectedAt;
    }
}
