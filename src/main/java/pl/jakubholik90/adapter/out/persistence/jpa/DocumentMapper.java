package pl.jakubholik90.adapter.out.persistence.jpa;

import org.springframework.stereotype.Component;
import pl.jakubholik90.adapter.out.persistence.jpa.entity.DocumentEntity;
import pl.jakubholik90.domain.model.Document;
import pl.jakubholik90.domain.model.DocumentStatus;
import pl.jakubholik90.domain.model.RecipientType;

import java.time.LocalDateTime;

@Component
public class DocumentMapper {

    public DocumentEntity mapToEntity(Document document) {
        int documentId = document.getDocumentId();
        String fileName = document.getFileName();
        Integer projectId = document.getProjectId();
        DocumentStatus status = document.getStatus();
        RecipientType currentRecipient = document.getCurrentRecipient();
        LocalDateTime lastStatusChange = document.getLastStatusChange();

        DocumentEntity returnEntity = new DocumentEntity(
                documentId,
                fileName,
                projectId,
                status,
                currentRecipient,
                lastStatusChange);
        return returnEntity;
    };

    public Document mapToDocument(DocumentEntity documentEntity) {
        Document returnDocument = new Document(documentEntity.getFileName(), documentEntity.getProjectId());
        return returnDocument;
    }
}
