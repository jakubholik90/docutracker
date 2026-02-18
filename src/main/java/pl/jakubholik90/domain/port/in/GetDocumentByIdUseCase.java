package pl.jakubholik90.domain.port.in;

import pl.jakubholik90.domain.model.Document;

import java.util.Optional;

public interface GetDocumentByIdUseCase {
    public Optional<Document> getDocumentById(Integer id);
}
