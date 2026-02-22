package pl.jakubholik90.adapter.in.web.dto;

import pl.jakubholik90.domain.model.DocumentStatus;
import pl.jakubholik90.domain.model.RecipientType;

import java.time.LocalDateTime;

public record StatusChangeEventResponse(
        LocalDateTime timestamp,
        DocumentStatus fromStatus,
        DocumentStatus toStatus,
        RecipientType fromRecipient,
        RecipientType toRecipient,
        String changedBy,
        String reason
) {

}
