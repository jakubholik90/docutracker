package pl.jakubholik90.domain.port.out;

import pl.jakubholik90.domain.model.Document;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository {

    public Document save(Document document);

    public Optional<Document> findByDocumentId(int documentId);

    public List<Document> findByProjectId(int projectId);

    public void deleteByDocumentId(int documentId);

    public boolean ifExistsByDocumentId(int documentId);

    public void deleteAll();

    public List<Document> findAll();

}
