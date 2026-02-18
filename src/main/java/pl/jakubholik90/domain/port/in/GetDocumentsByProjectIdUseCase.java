package pl.jakubholik90.domain.port.in;

import pl.jakubholik90.domain.common.PageRequest;
import pl.jakubholik90.domain.common.PageResult;
import pl.jakubholik90.domain.model.Document;

import java.util.List;

public interface GetDocumentsByProjectIdUseCase {

    public PageResult<Document> getDocumentsByProjectId(Integer projectId, PageRequest pageRequest);

}
