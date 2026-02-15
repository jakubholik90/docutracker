package pl.jakubholik90.domain.port.in;

import pl.jakubholik90.domain.model.Document;

import java.util.List;

public interface GetDocumentsByProjectIdUseCase {

    public List<Document> getDocumentsByProjectId(Integer projectId);

}
