package pl.jakubholik90.domain.model;

import pl.jakubholik90.infrastructure.exception.DocumentException;
import pl.jakubholik90.infrastructure.exception.ProjectException;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Document {

    // private static final AtomicInteger idCounter = new AtomicInteger(0);
    private int documentId;
    private String fileName;
    private Integer projectId;
    private DocumentStatus status;
    private RecipientType currentRecipient;
    private LocalDateTime lastStatusChange;

    public Document(String fileName, Integer projectId) {
        if (fileName == null) {
            throw new DocumentException("fileName cannot be null");
        }

        if (projectId == null) {
            throw new ProjectException("projectId cannot be null");
        }
        this.fileName = fileName;
        this.projectId = projectId;
        this.status=DocumentStatus.NONE;
        this.lastStatusChange= LocalDateTime.now();
    }

    // getters only, no setters
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

    public RecipientType getCurrentRecipient() {
        return currentRecipient;
    }
}
