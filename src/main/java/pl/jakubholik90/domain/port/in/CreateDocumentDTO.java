package pl.jakubholik90.domain.port.in;

import pl.jakubholik90.domain.model.RecipientType;

public record CreateDocumentDTO(String fileName, Integer projectId, RecipientType initialRecipient) {

}
