package pl.jakubholik90.domain.port.in;

import pl.jakubholik90.domain.model.Document;
import pl.jakubholik90.infrastructure.exception.DocumentException;
import pl.jakubholik90.infrastructure.exception.ProjectException;

public interface CreateDocumentUseCase {

    public Document createDocument(CreateDocumentDTO createDocumentDTO) throws DocumentException, ProjectException;

}
