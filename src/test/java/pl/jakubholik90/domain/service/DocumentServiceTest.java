package pl.jakubholik90.domain.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.jakubholik90.domain.common.PageRequest;
import pl.jakubholik90.domain.common.PageResult;
import pl.jakubholik90.domain.model.Document;
import pl.jakubholik90.domain.model.DocumentStatus;
import pl.jakubholik90.domain.model.RecipientType;
import pl.jakubholik90.domain.port.in.CreateDocumentDTO;
import pl.jakubholik90.domain.port.out.DocumentRepository;
import pl.jakubholik90.infrastructure.exception.DocumentException;
import pl.jakubholik90.infrastructure.exception.ProjectException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Test
    public void checkGetAllDocuments() {
        Document document0 = Document.builder().documentId(0).projectId(1).build();
        Document document1 = Document.builder().documentId(1).projectId(2).build();
        Document document2 = Document.builder().documentId(2).projectId(1).build();
        List<Document> listOfAllDocuments = new ArrayList<>();
        listOfAllDocuments.add(document0);
        listOfAllDocuments.add(document1);
        listOfAllDocuments.add(document2);
        when(documentRepository.findAll(any())).thenReturn(new PageResult<Document>(
                listOfAllDocuments,
                0,
                10,
                listOfAllDocuments.size(),
                1));
        Assertions.assertEquals(listOfAllDocuments,documentService.getAllDocuments(new PageRequest(0,10)).content());
    }

    @Test
    public void checkGetDocumentById() {
        when(documentRepository.findByDocumentId(1)).thenReturn(
                Optional.of(
                        Document.builder()
                                .documentId(1)
                                .build())
        );
    Assertions.assertEquals(1,documentService.getDocumentById(1).get().getDocumentId());
    }

    @Test
    public void checkGetDocumentsByProjectId() {
        Document document0 = Document.builder().documentId(0).projectId(1).build();
        Document document1 = Document.builder().documentId(1).projectId(2).build();
        Document document2 = Document.builder().documentId(2).projectId(1).build();
        List<Document> listOfDocumentsFromProject1 = new ArrayList<>();
        listOfDocumentsFromProject1.add(document0);
        listOfDocumentsFromProject1.add(document1);
        listOfDocumentsFromProject1.add(document2);

        when(documentRepository.findByProjectId(1)).thenReturn(listOfDocumentsFromProject1);

        Assertions.assertEquals(listOfDocumentsFromProject1,
                documentService.getDocumentsByProjectId(1));
    }

}
