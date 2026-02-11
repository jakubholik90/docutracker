package pl.jakubholik90.adapter.out.persistence.jpa;

import org.springframework.stereotype.Component;
import pl.jakubholik90.adapter.out.persistence.jpa.entity.DocumentEntity;
import pl.jakubholik90.domain.model.Document;
import pl.jakubholik90.domain.model.DocumentStatus;
import pl.jakubholik90.domain.model.RecipientType;

import java.time.LocalDateTime;

@Component
public class DocumentMapper {

    public static DocumentEntity mapToEntity(Document document) {
        String fileName = document.getFileName();
        Integer projectId = document.getProjectId();
        DocumentStatus status = document.getStatus();
        RecipientType currentRecipient = document.getCurrentRecipient();
        LocalDateTime lastStatusChange = document.getLastStatusChange();

        DocumentEntity returnEntity = new DocumentEntity();

        if(document.getDocumentId()!=null) {
            returnEntity.setDocumentId(returnEntity.getDocumentId());
        } //  jpa will handle automatically if documentId==null
        returnEntity.setFileName(fileName);
        returnEntity.setProjectId(projectId);
        returnEntity.setStatus(status);
        returnEntity.setCurrentRecipient(currentRecipient);
        returnEntity.setLastStatusChange(lastStatusChange);

        return returnEntity;
    };

    public static Document mapToDocument(DocumentEntity documentEntity) {
        Document returnDocument = Document.builder()
                .documentId(documentEntity.getDocumentId())
                .fileName(documentEntity.getFileName())
                .projectId(documentEntity.getProjectId())
                .status(documentEntity.getStatus())
                .currentRecipient(documentEntity.getCurrentRecipient())
                .lastStatusChange(documentEntity.getLastStatusChange())
                .build();

        return returnDocument;
    }
}
