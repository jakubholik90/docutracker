package pl.jakubholik90.adapter.out.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.jakubholik90.adapter.out.persistence.jpa.DocumentJpaRepository;
import pl.jakubholik90.adapter.out.persistence.jpa.DocumentMapper;
import pl.jakubholik90.adapter.out.persistence.jpa.entity.DocumentEntity;
import pl.jakubholik90.domain.model.Document;
import pl.jakubholik90.domain.port.out.DocumentRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class DocumentRepositoryAdapter implements DocumentRepository {

    private DocumentJpaRepository documentJpaRepository;

    private DocumentMapper documentMapper;

    @Autowired
    public DocumentRepositoryAdapter(DocumentJpaRepository documentJpaRepository, DocumentMapper documentMapper) {
        this.documentJpaRepository = documentJpaRepository;
        this.documentMapper = documentMapper;
    }

    @Override
    public void save(Document document) {
        DocumentEntity documentEntity = documentMapper.mapToEntity(document);
        documentJpaRepository.save(documentEntity);
    }

    @Override
    public Optional<Document> findByDocumentId(int documentId) {
        return Optional.empty();
    }

    @Override
    public List<Document> findByProjectId(int projectId) {
        return List.of();
    }

    @Override
    public void deleteByDocumentId(int documentId) {
    }

    @Override
    public boolean ifExistsByDocumentId(int documentId) {
        return false;
    }
}
