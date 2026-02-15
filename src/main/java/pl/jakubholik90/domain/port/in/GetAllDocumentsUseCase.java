package pl.jakubholik90.domain.port.in;

import pl.jakubholik90.domain.model.Document;
import pl.jakubholik90.domain.common.PageRequest;
import pl.jakubholik90.domain.common.PageResult;

public interface GetAllDocumentsUseCase {

    public PageResult<Document> execute(PageRequest pageRequest);

}
