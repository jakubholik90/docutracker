package pl.jakubholik90.domain.service;

import pl.jakubholik90.domain.common.PageRequest;
import pl.jakubholik90.domain.common.PageResult;
import pl.jakubholik90.domain.model.Document;
import pl.jakubholik90.domain.model.DocumentStatus;
import pl.jakubholik90.domain.port.in.*;
import pl.jakubholik90.domain.port.out.DocumentRepository;
import pl.jakubholik90.infrastructure.exception.DocumentException;
import pl.jakubholik90.infrastructure.exception.ProjectException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class DocumentService implements CreateDocumentUseCase,
        GetAllDocumentsUseCase,
        GetDocumentByIdUseCase,
        GetDocumentsByProjectIdUseCase {

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

    @Override
    public PageResult<Document> getAllDocuments(PageRequest pageRequest) {
        return documentRepository.findAll(pageRequest);
    }

    @Override
    public Optional<Document> getDocumentById(Integer id) {
        return documentRepository.findByDocumentId(id);
    }

    @Override
    public PageResult<Document> getDocumentsByProjectId(Integer projectId, PageRequest pageRequest) {
        return documentRepository.findByProjectId(projectId,pageRequest);
    }
}
