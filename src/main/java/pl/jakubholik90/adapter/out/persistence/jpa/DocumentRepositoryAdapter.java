package pl.jakubholik90.adapter.out.persistence.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.jakubholik90.adapter.out.persistence.jpa.entity.DocumentEntity;
import pl.jakubholik90.domain.common.PageRequest;
import pl.jakubholik90.domain.common.PageResult;
import pl.jakubholik90.domain.model.Document;
import pl.jakubholik90.domain.port.out.DocumentRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class DocumentRepositoryAdapter implements DocumentRepository {

    private final DocumentJpaRepository documentJpaRepository;

    private final DocumentMapper documentMapper;

    public DocumentRepositoryAdapter(DocumentJpaRepository documentJpaRepository, DocumentMapper documentMapper) {
        this.documentJpaRepository = documentJpaRepository;
        this.documentMapper = documentMapper;
    }

    @Override
    public Document save(Document document) {
        DocumentEntity documentEntity = documentMapper.mapToEntity(document); // Id == null
        DocumentEntity savedEntity = documentJpaRepository.save(documentEntity); // JPA creates new ID != null
        Document savedDocument = DocumentMapper.mapToDocument(savedEntity); // Id != null
        return savedDocument;
    }

    @Override
    public void deleteAll() {
        documentJpaRepository.deleteAll();
    }

    @Override
    public int count() {
        return (int) documentJpaRepository.count();
    }

    @Override
    public PageResult<Document> findAll(PageRequest pageRequest) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.page(),
                pageRequest.size()
        );
        Page<DocumentEntity> documentEntityPage = documentJpaRepository.findAll(pageable);
        List<Document> documentList = documentEntityPage.getContent()
                .stream()
                .map(DocumentMapper::mapToDocument)
                .toList();
        PageResult<Document> pageResult = new PageResult<>(
                documentList,
                documentEntityPage.getNumber(),
                documentEntityPage.getSize(),
                documentEntityPage.getTotalElements(),
                documentEntityPage.getTotalPages());
        return pageResult;
    }

    @Override
    public Optional<Document> findByDocumentId(int documentId) {
        Optional<DocumentEntity> entityOptional = documentJpaRepository.findById(documentId);
        Optional<Document> returnOptionalDocument;
        if(entityOptional.isEmpty()) {
            returnOptionalDocument = Optional.empty();
        } else {
            returnOptionalDocument = Optional.of(DocumentMapper.mapToDocument(entityOptional.get()));
        }
        return returnOptionalDocument;
    }

    @Override
    public PageResult<Document> findByProjectId(int projectId, PageRequest pageRequest) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.page(),
                pageRequest.size()
        );
        Page<DocumentEntity> documentEntityPage = documentJpaRepository.findByProjectId(projectId, pageable);
        List<Document> documentList = documentEntityPage.getContent()
                .stream()
                .map(DocumentMapper::mapToDocument)
                .toList();
        PageResult<Document> pageResult = new PageResult<>(
                documentList,
                documentEntityPage.getNumber(),
                documentEntityPage.getSize(),
                documentEntityPage.getTotalElements(),
                documentEntityPage.getTotalPages());
        return pageResult;
    }

    @Override
    public void deleteByDocumentId(int documentId) {
        Optional<DocumentEntity> entityById = documentJpaRepository.findById(documentId);
        boolean isEmpty = entityById.isEmpty();
        if (!isEmpty) {
            documentJpaRepository.deleteById(documentId);
        }
    }

    @Override
    public boolean ifExistsByDocumentId(int documentId) {
        boolean returnBool = documentJpaRepository.existsById(documentId);
        return returnBool;
    }
}
