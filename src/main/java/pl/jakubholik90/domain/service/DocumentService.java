package pl.jakubholik90.domain.service;

import pl.jakubholik90.domain.model.Document;
import pl.jakubholik90.domain.model.DocumentStatus;
import pl.jakubholik90.domain.port.in.CreateDocumentDTO;
import pl.jakubholik90.domain.port.in.CreateDocumentUseCase;
import pl.jakubholik90.domain.port.out.DocumentRepository;
import pl.jakubholik90.infrastructure.exception.DocumentException;
import pl.jakubholik90.infrastructure.exception.ProjectException;

import java.time.LocalDateTime;

public class DocumentService implements CreateDocumentUseCase {

    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public Document createDocument(CreateDocumentDTO createDocumentDTO) throws DocumentException, ProjectException {
        if (createDocumentDTO.fileName() == null) {
            throw new DocumentException("fileName cannot be null");
        }

        if (createDocumentDTO.projectId() == null) {
            throw new ProjectException("projectId cannot be null");
        }

        Document newDocument = Document.builder()
                .fileName(createDocumentDTO.fileName())
                .projectId(createDocumentDTO.projectId())
                .status(DocumentStatus.DRAFT)
                .currentRecipient(createDocumentDTO.initialRecipient())
                .lastStatusChange(LocalDateTime.now())
                        .build();

        Document savedDocument = documentRepository.save(newDocument);

        return savedDocument;
    }
}
