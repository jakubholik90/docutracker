package pl.jakubholik90.adapter.in.web.dto;

import pl.jakubholik90.domain.model.DocumentStatus;
import pl.jakubholik90.domain.model.RecipientType;

public record StatusChangeEventRequest(
        DocumentStatus newStatus,
        RecipientType newRecipient,
        String reason){
}
