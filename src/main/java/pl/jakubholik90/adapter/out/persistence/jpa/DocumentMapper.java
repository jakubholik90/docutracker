package pl.jakubholik90.adapter.out.persistence.jpa;

import org.springframework.stereotype.Component;
import pl.jakubholik90.adapter.out.persistence.jpa.entity.DocumentEntity;
import pl.jakubholik90.adapter.out.persistence.jpa.entity.StatusChangeEventEntity;
import pl.jakubholik90.domain.model.Document;
import pl.jakubholik90.domain.model.DocumentStatus;
import pl.jakubholik90.domain.model.RecipientType;
import pl.jakubholik90.domain.model.StatusChangeEvent;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DocumentMapper {

    public static DocumentEntity mapToEntity(Document document) {
        String fileName = document.getFileName();
        Integer projectId = document.getProjectId();
        DocumentStatus status = document.getStatus();
        RecipientType currentRecipient = document.getCurrentRecipient();
        LocalDateTime lastStatusChange = document.getLastStatusChange();
        List<StatusChangeEventEntity> history = document.getHistory().stream()
                .map(StatusChangeEventMapper::mapToEntity)
                .toList();

        DocumentEntity returnEntity = new DocumentEntity();

        if(document.getDocumentId()!=null) {
            returnEntity.setDocumentId(document.getDocumentId());
        } //  jpa will handle automatically if documentId==null
        returnEntity.setFileName(fileName);
        returnEntity.setProjectId(projectId);
        returnEntity.setStatus(status);
        returnEntity.setCurrentRecipient(currentRecipient);
        returnEntity.setLastStatusChange(lastStatusChange);
        // setting document for each of StatusChangeEvent in history
        history.forEach(statusChangeEventEntity -> statusChangeEventEntity.setDocument(returnEntity));
        returnEntity.setHistory(history);

        return returnEntity;
    };

    public static Document mapToDocument(DocumentEntity documentEntity) {
        List<StatusChangeEvent> history = documentEntity.getHistory().stream()
                .map(StatusChangeEventMapper::mapToEvent)
                .toList();


        Document returnDocument = Document.builder()
                .documentId(documentEntity.getDocumentId())
                .fileName(documentEntity.getFileName())
                .projectId(documentEntity.getProjectId())
                .status(documentEntity.getStatus())
                .currentRecipient(documentEntity.getCurrentRecipient())
                .lastStatusChange(documentEntity.getLastStatusChange())
                .history(history)
                .build();

        return returnDocument;
    }
}
