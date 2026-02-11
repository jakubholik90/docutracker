package pl.jakubholik90.adapter.out.persistence.jpa.entity;
import jakarta.persistence.*;
import pl.jakubholik90.domain.model.DocumentStatus;
import pl.jakubholik90.domain.model.RecipientType;
import java.time.LocalDateTime;
@Entity
@Table(name = "documents") //table definition
public class DocumentEntity {
    //column defitions
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Integer documentId;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "project_id", nullable = false)
    private Integer projectId;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_status", nullable = false, length = 50)
    private DocumentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_recipient", nullable = false, length = 50)
    private RecipientType currentRecipient;

    @Column(name = "last_status_change", nullable = false)
    private LocalDateTime lastStatusChange;

    // constructors
    public DocumentEntity() {
    }
    public DocumentEntity(Integer documentId, String fileName, Integer projectId, DocumentStatus status, RecipientType currentRecipient, LocalDateTime lastStatusChange) {
        this.documentId = documentId;
        this.fileName = fileName;
        this.projectId = projectId;
        this.status = status;
        this.currentRecipient = currentRecipient;
        this.lastStatusChange = lastStatusChange;
    }

    // getters
    public Integer getDocumentId() {
        return documentId;
    }
    public String getFileName() {
        return fileName;
    }
    public Integer getProjectId() {
        return projectId;
    }
    public DocumentStatus getStatus() {
        return status;
    }
    public RecipientType getCurrentRecipient() {
        return currentRecipient;
    }
    public LocalDateTime getLastStatusChange() {
        return lastStatusChange;
    }

    // setters
    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
    public void setStatus(DocumentStatus status) {
        this.status = status;
    }
    public void setCurrentRecipient(RecipientType currentRecipient) {
        this.currentRecipient = currentRecipient;
    }
    public void setLastStatusChange(LocalDateTime lastStatusChange) {
        this.lastStatusChange = lastStatusChange;
    }
}
