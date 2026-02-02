package pl.jakubholik90.domain.port.out;

import pl.jakubholik90.domain.model.Document;

public interface DocumentRepository {

    public void save(Document document);

}
