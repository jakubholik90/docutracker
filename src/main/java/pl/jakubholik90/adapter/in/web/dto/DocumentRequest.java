package pl.jakubholik90.adapter.in.web.dto;

import pl.jakubholik90.domain.model.RecipientType;

public record DocumentRequest (
        String fileName,
        Integer projectId,
        RecipientType initialRecipient
) {
}
