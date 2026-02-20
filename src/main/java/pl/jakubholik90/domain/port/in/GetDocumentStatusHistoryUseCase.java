package pl.jakubholik90.domain.port.in;

import pl.jakubholik90.domain.model.Document;
import pl.jakubholik90.domain.model.StatusChangeEvent;

import java.util.List;

public interface GetDocumentStatusHistoryUseCase {
    public List<StatusChangeEvent> getDocumentStatusHistory(int documentId);
}
