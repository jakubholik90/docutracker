package pl.jakubholik90.adapter.out.persistence.jpa.entity;


import jakarta.persistence.*;
import pl.jakubholik90.domain.model.DocumentStatus;
import pl.jakubholik90.domain.model.RecipientType;

import java.time.LocalDateTime;

@Entity
@Table(name = "document_status_history")
public class StatusChangeEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "document")
    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)
    DocumentEntity document;

    @Column(name = "timestamp", nullable = false)
    LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "fromStatus", nullable = false)
    DocumentStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "toStatus", nullable = false)
    DocumentStatus toStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "fromRecipient",nullable = false)
    RecipientType fromRecipient;

    @Enumerated(EnumType.STRING)
    @Column(name = "toRecipient", nullable = false)
    RecipientType toRecipient;

    @Column(name = "changed_by", nullable = false)
    String changedBy;

    @Column(name = "reason", nullable = false)
    String reason;

    // constructors
    public StatusChangeEventEntity() {
    }

    public StatusChangeEventEntity(Long id, DocumentEntity document, LocalDateTime timestamp, DocumentStatus fromStatus, DocumentStatus toStatus, RecipientType fromRecipient, RecipientType toRecipient, String changedBy, String reason) {
        this.id = id;
        this.document = document;
        this.timestamp = timestamp;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.fromRecipient = fromRecipient;
        this.toRecipient = toRecipient;
        this.changedBy = changedBy;
        this.reason = reason;
    }

    //getters
    public Long getId() {
        return id;
    }

    public DocumentEntity getDocument() {
        return document;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public DocumentStatus getFromStatus() {
        return fromStatus;
    }

    public DocumentStatus getToStatus() {
        return toStatus;
    }

    public RecipientType getFromRecipient() {
        return fromRecipient;
    }

    public RecipientType getToRecipient() {
        return toRecipient;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public String getReason() {
        return reason;
    }

    //setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public void setToRecipient(RecipientType toRecipient) {
        this.toRecipient = toRecipient;
    }

    public void setFromRecipient(RecipientType fromRecipient) {
        this.fromRecipient = fromRecipient;
    }

    public void setToStatus(DocumentStatus toStatus) {
        this.toStatus = toStatus;
    }

    public void setFromStatus(DocumentStatus fromStatus) {
        this.fromStatus = fromStatus;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setDocument(DocumentEntity document) {
        this.document = document;
    }
}
