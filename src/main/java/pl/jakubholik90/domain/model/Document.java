package pl.jakubholik90.domain.model;

import lombok.Builder;
import pl.jakubholik90.infrastructure.exception.DocumentException;
import pl.jakubholik90.infrastructure.exception.ProjectException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Builder
public class Document {
    private Integer documentId; // Integer bo domyslna wartosc to null a przy int to 0
    private String fileName;
    private Integer projectId;
    private DocumentStatus status;
    private RecipientType currentRecipient;
    private LocalDateTime lastStatusChange;

    @Builder.Default
    private List<StatusChangeEvent> history = new ArrayList<>();

    // getters
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

    public List<StatusChangeEvent> getHistory() {
        return history;
    }

    public void addStatusChangeEvent(StatusChangeEvent event) {
        this.history.add(event);
    }


    // setters for mutable fields only


    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public void setCurrentRecipient(RecipientType currentRecipient) {
        this.currentRecipient = currentRecipient;
    }

    public void setLastStatusChange(LocalDateTime lastStatusChange) {
        this.lastStatusChange = lastStatusChange;
    }

    @Override
    public String toString() {
        return "Document{" +
                "documentId=" + documentId +
                ", fileName='" + fileName + '\'' +
                ", projectId=" + projectId +
                '}';
    }


    // overriding equals() & hashCode() in order to List.contains(Document) working (comparing by documentId)
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Document document)) return false;
        return Objects.equals(getDocumentId(), document.getDocumentId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDocumentId());
    }
}
