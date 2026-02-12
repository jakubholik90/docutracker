package pl.jakubholik90.domain.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.jakubholik90.domain.model.Document;
import pl.jakubholik90.domain.model.DocumentStatus;
import pl.jakubholik90.domain.model.RecipientType;
import pl.jakubholik90.domain.port.in.CreateDocumentDTO;
import pl.jakubholik90.domain.port.out.DocumentRepository;
import pl.jakubholik90.infrastructure.exception.DocumentException;
import pl.jakubholik90.infrastructure.exception.ProjectException;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class DocumentServiceTest {

    private DocumentService documentService;
    private DocumentRepository documentRepository;

    @BeforeEach
            public void setUp() {
        documentRepository = Mockito.mock(DocumentRepository.class);
        documentService = new DocumentService(documentRepository);
        when(documentRepository.save(any(Document.class))).thenAnswer(
                invocation -> {
                    Document saved = invocation.getArgument(0);
                    return Document.builder()
                            .documentId(1)
                            .fileName(saved.getFileName())
                            .projectId(saved.getProjectId())
                            .status(saved.getStatus())
                            .currentRecipient(saved.getCurrentRecipient())
                            .lastStatusChange(saved.getLastStatusChange())
                            .build();
                });
        }

    @Test
    public void shouldCreateDocument() {
        CreateDocumentDTO createDocumentDTO = new CreateDocumentDTO("test.pdf", 1, RecipientType.SUBCONTRACTOR);
        Document document = documentService.createDocument(createDocumentDTO);
        Assertions.assertEquals(DocumentStatus.DRAFT,document.getStatus());
        Assertions.assertEquals(createDocumentDTO.initialRecipient(),document.getCurrentRecipient());
        Assertions.assertTrue(document.getLastStatusChange().isAfter(LocalDateTime.now().minusSeconds(5)));
        Assertions.assertEquals(createDocumentDTO.fileName(),document.getFileName());
        Assertions.assertEquals(createDocumentDTO.projectId(),document.getProjectId());
        Mockito.verify(documentRepository,times(1)).save(any(Document.class));
        Assertions.assertNotNull(document.getDocumentId());
    }

    @Test
    public void shouldSetCurrentTimestampOnCreation() {
        CreateDocumentDTO createDocumentDTO = new CreateDocumentDTO("test.pdf", 1, RecipientType.SUBCONTRACTOR);
        Document document = documentService.createDocument(createDocumentDTO);
        boolean bool = (
                document.getLastStatusChange().isAfter(LocalDateTime.now().minusSeconds(1))
                &&
                document.getLastStatusChange().isBefore(LocalDateTime.now().plusSeconds(1)));
        Assertions.assertTrue(bool);
    }

    @Test
    public void shouldThrowExceptionWhenFileNameIsNull() {
        CreateDocumentDTO createDocumentDTO = new CreateDocumentDTO(null, 1, RecipientType.SUBCONTRACTOR);
        Assertions.assertThrows(DocumentException.class,() -> documentService.createDocument(createDocumentDTO));
    }

    @Test
    public void shouldThrowExceptionWhenProjectIdIsNull() {
        CreateDocumentDTO createDocumentDTO = new CreateDocumentDTO("test.pdf", null, RecipientType.SUBCONTRACTOR);
        Assertions.assertThrows(ProjectException.class,() -> documentService.createDocument(createDocumentDTO));
    }

}
