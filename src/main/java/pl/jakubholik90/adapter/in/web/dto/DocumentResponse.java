package pl.jakubholik90.adapter.in.web.dto;

import pl.jakubholik90.domain.model.DocumentStatus;
import pl.jakubholik90.domain.model.RecipientType;

import java.time.LocalDateTime;

public record DocumentResponse (Integer id, String fileName, Integer projectId, DocumentStatus status, RecipientType currentRecipient, LocalDateTime lastStatusChange) {
}
