package pl.jakubholik90.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import pl.jakubholik90.domain.model.DocumentStatus;
import pl.jakubholik90.domain.model.RecipientType;

import java.time.LocalDateTime;

public record DocumentResponse (
        Integer id,
        String fileName,
        Integer projectId,
        DocumentStatus status,
        RecipientType currentRecipient,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime lastStatusChange) {

        public DocumentResponse(Integer id) {
                this(id, "", 0, null, null, null);
        }
}
