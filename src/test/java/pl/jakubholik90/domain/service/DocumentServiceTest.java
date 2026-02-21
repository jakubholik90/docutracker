package pl.jakubholik90.domain.service;

import lombok.Builder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.jakubholik90.domain.common.PageRequest;
import pl.jakubholik90.domain.common.PageResult;
import pl.jakubholik90.domain.model.Document;
import pl.jakubholik90.domain.model.DocumentStatus;
import pl.jakubholik90.domain.model.RecipientType;
import pl.jakubholik90.domain.model.StatusChangeEvent;
import pl.jakubholik90.domain.port.in.ChangeDocumentStatusDTO;
import pl.jakubholik90.domain.port.in.CreateDocumentDTO;
import pl.jakubholik90.domain.port.out.DocumentRepository;
import pl.jakubholik90.infrastructure.exception.DocumentException;
import pl.jakubholik90.infrastructure.exception.ProjectException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
                            .history(saved.getHistory())
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
        int projectId = 1;
        Document document0 = Document.builder().documentId(0).projectId(projectId).build();
        Document document1 = Document.builder().documentId(1).projectId(projectId).build();
        Document document2 = Document.builder().documentId(2).projectId(projectId).build();
        List<Document> listOfDocumentsFromProject1 = new ArrayList<>();
        listOfDocumentsFromProject1.add(document0);
        listOfDocumentsFromProject1.add(document1);
        listOfDocumentsFromProject1.add(document2);

        int page = 0;
        int size = listOfDocumentsFromProject1.size()-1;

        List<Document> listOfDocumentsFromProject1Paginated = List.of(document0,document1);
        PageRequest pageRequest = new PageRequest(page,size);
        PageResult<Document> pageResult = new PageResult<>(
                listOfDocumentsFromProject1Paginated,
                page,
                size,
                listOfDocumentsFromProject1.size(),
                (int) Math.ceil((double) listOfDocumentsFromProject1.size() / size));
        System.out.println("pageResult:" + pageResult);

        when(documentRepository.findByProjectId(projectId,pageRequest)).thenReturn(pageResult);

        Assertions.assertEquals(listOfDocumentsFromProject1Paginated,
                documentService.getDocumentsByProjectId(projectId,pageRequest).content());
    }

    @Test
    public void shouldChangeDocumentStatus() {
        //given
        StatusChangeEvent firstEvent = StatusChangeEvent.builder()
                .timestamp(LocalDateTime.of(1995, 12, 31, 12, 59, 00))
                .fromStatus(DocumentStatus.AT_SUBCONTRACTOR)
                .toStatus(DocumentStatus.AT_USER)
                .fromRecipient(RecipientType.SUBCONTRACTOR)
                .toRecipient(RecipientType.USER)
                .changedBy("SYSTEM")
                .reason("test")
                .build();

        ArrayList<StatusChangeEvent> history = new ArrayList<>();
        history.add(firstEvent);

        Document oldDocument = Document.builder()
                .documentId(1)
                .fileName("test.pdf")
                .projectId(10)
                .status(DocumentStatus.DRAFT)
                .currentRecipient(RecipientType.USER)
                .lastStatusChange(LocalDateTime.of(2000, 12, 31, 12, 59, 00))
                .history(history )
                .build();

        Integer documentId = oldDocument.getDocumentId();

        int documentHistorySize = oldDocument
                .getHistory()
                .size();

        ChangeDocumentStatusDTO changeStatusDTO = new ChangeDocumentStatusDTO(
                documentId,
                DocumentStatus.AT_USER,
                RecipientType.USER,
                "setup",
                "SYSTEM");
        when(documentRepository.findByDocumentId(documentId)).thenReturn(Optional.of(oldDocument));

        DocumentStatus oldStatus = oldDocument.getStatus();
        RecipientType oldRecipient = oldDocument.getCurrentRecipient();
        //when
        Document updatedDocument = documentService.changeDocumentStatus(changeStatusDTO);
        //then
        StatusChangeEvent last = updatedDocument.getHistory().getLast();
        Assertions.assertTrue(updatedDocument.getHistory().size()>documentHistorySize);
        // Assertions.assertTrue(last.getId()!=null);  not checked, ID != null only after using real JPA (outside of tests)
        Assertions.assertTrue(last.getTimestamp()!=null);
        Assertions.assertEquals(oldStatus,last.getFromStatus());
        Assertions.assertEquals(DocumentStatus.AT_USER, last.getToStatus());
        Assertions.assertEquals(oldRecipient,last.getFromRecipient());
        Assertions.assertEquals(RecipientType.USER, last.getToRecipient());
        Assertions.assertEquals("SYSTEM",last.getChangedBy()); //test hardcoded, field is missing
        Assertions.assertEquals("setup", last.getReason());
    }

    @Test
    public void shouldThrowExceptionWhenDocumentNotFound() {
        //given
        Integer documentId = 1;
        when(documentRepository.findByDocumentId(documentId)).thenReturn(Optional.empty());
        ChangeDocumentStatusDTO changeStatusDTO = new ChangeDocumentStatusDTO(
                documentId,
                DocumentStatus.AT_USER,
                RecipientType.USER,
                "setup",
                "SYSTEM");
        //when+then
        Assertions.assertThrows(DocumentException.class,() -> documentService.changeDocumentStatus(changeStatusDTO));
    }

    @Test
    public void shouldReturnListOfStatusChangeEvents() {
        //given
        int id = 1;
        ArrayList<StatusChangeEvent> history = new ArrayList<>();
        StatusChangeEvent event0 = StatusChangeEvent.builder()
                .id(0L)
                .timestamp(LocalDateTime.now())
                .fromStatus(DocumentStatus.AT_USER)
                .toStatus(DocumentStatus.CHECKING_BY_CLIENT)
                .fromRecipient(RecipientType.USER)
                .toRecipient(RecipientType.CLIENT)
                .changedBy("SYSTEM")
                .reason("test0")
                .build();
        StatusChangeEvent event1 = StatusChangeEvent.builder()
                .id(1L)
                .timestamp(LocalDateTime.now())
                .fromStatus(DocumentStatus.AT_USER)
                .toStatus(DocumentStatus.CHECKING_BY_CLIENT)
                .fromRecipient(RecipientType.USER)
                .toRecipient(RecipientType.CLIENT)
                .changedBy("SYSTEM")
                .reason("test1")
                .build();
        StatusChangeEvent event2 = StatusChangeEvent.builder()
                .id(2L)
                .timestamp(LocalDateTime.now())
                .fromStatus(DocumentStatus.AT_USER)
                .toStatus(DocumentStatus.CHECKING_BY_CLIENT)
                .fromRecipient(RecipientType.USER)
                .toRecipient(RecipientType.CLIENT)
                .changedBy("SYSTEM")
                .reason("test2")
                .build();
        history.add(event0);
        history.add(event1);
        history.add(event2);
        Document build = Document.builder()
                .documentId(id)
                .history(history)
                .build();
        Optional<Document> optionalDocument = Optional.of(build);


        when(documentRepository.findByDocumentId(id)).thenReturn(optionalDocument);
        //when
        List<StatusChangeEvent> documentStatusHistory = documentService.getDocumentStatusHistory(id);
        System.out.println("documentStatusHistory:" + documentStatusHistory);
        //then
        Assertions.assertEquals(history,documentStatusHistory);
    }

}
