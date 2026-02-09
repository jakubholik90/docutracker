package pl.jakubholik90.adapter.out.persistence;

import pl.jakubholik90.domain.model.Document;
import pl.jakubholik90.domain.port.out.DocumentRepository;

import java.util.List;
import java.util.Optional;

public class DocumentRepositoryAdapter implements DocumentRepository {
    @Override
    public void save(Document document) {
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
