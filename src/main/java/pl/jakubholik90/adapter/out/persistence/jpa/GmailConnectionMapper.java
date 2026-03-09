package pl.jakubholik90.adapter.out.persistence.jpa;

import org.springframework.stereotype.Component;
import pl.jakubholik90.adapter.out.persistence.jpa.entity.GmailConnectionEntity;
import pl.jakubholik90.adapter.out.persistence.jpa.entity.UserEntity;
import pl.jakubholik90.domain.model.GmailConnection;
import pl.jakubholik90.domain.model.User;

import java.time.LocalDateTime;

@Component
public class GmailConnectionMapper {

    public static GmailConnection mapToConnection(GmailConnectionEntity gmailConnectionEntity) {
        GmailConnection returnConnection = GmailConnection.builder()
                .userId(gmailConnectionEntity.getUserId())
                .email(gmailConnectionEntity.getEmail())
                .accessToken(gmailConnectionEntity.getAccessToken())
                .refreshToken(gmailConnectionEntity.getRefreshToken())
                .tokenExpiry(gmailConnectionEntity.getTokenExpiry())
                .connectedAt(gmailConnectionEntity.getTokenExpiry())
                .build();

        return returnConnection;
    }

    public static GmailConnectionEntity mapToEntity(GmailConnection gmailConnection) {
        Long id = gmailConnection.getId();

        GmailConnectionEntity connectionEntity = new GmailConnectionEntity();

        if (id != null) {
          connectionEntity.setId(id);
        }
        connectionEntity.setUserId(gmailConnection.getUserId());
        connectionEntity.setEmail(gmailConnection.getEmail());
        connectionEntity.setAccessToken(gmailConnection.getAccessToken());
        connectionEntity.setRefreshToken(gmailConnection.getRefreshToken());
        connectionEntity.setTokenExpiry(gmailConnection.getTokenExpiry());
        connectionEntity.setConnectedAt(gmailConnection.getConnectedAt());

        return connectionEntity;
    }
}