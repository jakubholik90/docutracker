package pl.jakubholik90.domain.port.in;

import pl.jakubholik90.domain.model.DocumentStatus;
import pl.jakubholik90.domain.model.RecipientType;

public record ChangeDocumentStatusDTO(
        int documentId,
        DocumentStatus newStatus,
        RecipientType newRecipient,
        String reason) {
}
