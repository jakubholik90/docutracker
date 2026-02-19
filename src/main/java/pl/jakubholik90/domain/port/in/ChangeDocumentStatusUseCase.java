package pl.jakubholik90.domain.port.in;

import pl.jakubholik90.domain.model.Document;

public interface ChangeDocumentStatusUseCase {
    public Document changeDocumentStatus(ChangeDocumentStatusDTO changeDocumentStatusDTO);
}
