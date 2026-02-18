package pl.jakubholik90.domain.port.out;

import pl.jakubholik90.domain.common.PageRequest;
import pl.jakubholik90.domain.common.PageResult;
import pl.jakubholik90.domain.model.Document;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository {

    public Document save(Document document);

    public Optional<Document> findByDocumentId(int documentId);

    public PageResult<Document> findByProjectId(int projectId, PageRequest pageRequest);

    public void deleteByDocumentId(int documentId);

    public boolean ifExistsByDocumentId(int documentId);

    public void deleteAll();

    public PageResult<Document> findAll(PageRequest pageRequest);

    public int count();


}
