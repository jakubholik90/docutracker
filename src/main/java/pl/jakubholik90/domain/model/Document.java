package pl.jakubholik90.domain.model;

import pl.jakubholik90.infrastructure.exception.DocumentException;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Document {

    private static final AtomicInteger idCounter = new AtomicInteger(0);
    private final int documentId;
    private String fileName;
    private Integer projectId;
    private DocumentStatus status;
    private RecipientType currentRecipient;
    private LocalDateTime lastStatusChange;

    public Document(String fileName, Integer projectId) {
        if (projectId == null || fileName == null) {
            throw new DocumentException("projectId and fileName cannot be null");
        }
        this.documentId = idCounter.getAndIncrement();
        this.fileName = fileName;
        this.projectId = projectId;
        this.status=DocumentStatus.NONE;
        this.lastStatusChange= LocalDateTime.now();
    }

    // getters
    public String getFileName() {
        return fileName;
    }

    public int getDocumentId() {
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

    // setters
    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public void setCurrentRecipient(RecipientType currentRecipient) {
        this.currentRecipient = currentRecipient;
    }




}
