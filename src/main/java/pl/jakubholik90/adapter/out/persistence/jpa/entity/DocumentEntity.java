package pl.jakubholik90.adapter.out.persistence.jpa.entity;

import pl.jakubholik90.domain.model.DocumentStatus;
import pl.jakubholik90.domain.model.RecipientType;

import java.time.LocalDateTime;

// @Entity
public class DocumentEntity {

    private Integer id;
    private String fileName;
    private Integer projectId;
    private DocumentStatus status;
    private RecipientType currentRecipient;
    private LocalDateTime lastStatusChange;


}
