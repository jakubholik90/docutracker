package pl.jakubholik90.domain.model;

import lombok.Builder;
import pl.jakubholik90.infrastructure.exception.DocumentException;
import pl.jakubholik90.infrastructure.exception.ProjectException;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Builder
public class Document {
    private Integer documentId; // Integer bo domyslna wartosc to null a przy int to 0
    private String fileName;
    private Integer projectId;
    private DocumentStatus status;
    private RecipientType currentRecipient;
    private LocalDateTime lastStatusChange;

    // getters only, no setters
    public String getFileName() {
        return fileName;
    }

    public Integer getDocumentId() {
        return documentId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public LocalDateTime getLastStatusChange() {
        return lastStatusChange;
    }

    public RecipientType getCurrentRecipient() {
        return currentRecipient;
    }
}
