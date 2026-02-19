package pl.jakubholik90.adapter.out.persistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.jakubholik90.adapter.out.persistence.jpa.DocumentMapper;
import pl.jakubholik90.adapter.out.persistence.jpa.DocumentRepositoryAdapter;
import pl.jakubholik90.domain.common.PageRequest;
import pl.jakubholik90.domain.common.PageResult;
import pl.jakubholik90.domain.model.Document;
import pl.jakubholik90.domain.model.DocumentStatus;
import pl.jakubholik90.domain.model.RecipientType;
import pl.jakubholik90.domain.model.StatusChangeEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@DataJpaTest
@Testcontainers //handling test containers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DocumentRepositoryAdapter.class, DocumentMapper.class})
public class DocumentRepositoryAdapterTest {

    @Autowired
    private DocumentRepositoryAdapter documentRepositoryAdapter;

    private Document document1; // new document, before saving by JPA, with Id==null
    private Document document2; // new document, before saving by JPA, with Id==null
    private Document savedDocument1; // saved document, with Id!=null
    private Document savedDocument2; // saved document, with Id!=null

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    public void setUp() {
        documentRepositoryAdapter.deleteAll();
        document1 = Document.builder()
                .currentRecipient(RecipientType.SUBCONTRACTOR)
                .fileName("Test1.pdf")
                .lastStatusChange(LocalDateTime.now())
                .projectId(1)
                .status(DocumentStatus.AT_SUBCONTRACTOR)
                .build();
        document2 = Document.builder()
                .currentRecipient(RecipientType.INTERNAL_REVIEWER)
                .fileName("Test2.pdf")
                .lastStatusChange(LocalDateTime.of(2020,12,31,12,30))
                .projectId(2)
                .status(DocumentStatus.AT_USER)
                .build();
        savedDocument1 = documentRepositoryAdapter.save(document1); // JPa creates documentId here
        savedDocument2 = documentRepositoryAdapter.save(document2); // JPa creates documentId here
    }


    @Test
    public void checkIfExistsById() {
        boolean ifExists = documentRepositoryAdapter.ifExistsByDocumentId(savedDocument1.getDocumentId());
        Assertions.assertTrue(ifExists);
    }

    @Test
    public void shouldSaveDocument() {
        boolean b = documentRepositoryAdapter.ifExistsByDocumentId(savedDocument1.getDocumentId());
        System.out.println("test shouldSaveDocument(), savedDocument1.getDocumentId(): " + savedDocument1.getDocumentId());
        Assertions.assertTrue(b);
    }

    @Test
    public void shouldSaveDocumentWithHistory() {
        //given
        StatusChangeEvent event0 = StatusChangeEvent.builder()
                .timestamp(LocalDateTime.now())
                .fromStatus(DocumentStatus.AT_USER)
                .toStatus(DocumentStatus.AT_SUBCONTRACTOR)
                .fromRecipient(RecipientType.INTERNAL_REVIEWER)
                .toRecipient(RecipientType.SUBCONTRACTOR)
                .changedBy("admin")
                .reason("issue")
                .build();
        StatusChangeEvent event1 = StatusChangeEvent.builder()
                .timestamp(LocalDateTime.now())
                .fromStatus(DocumentStatus.AT_USER)
                .toStatus(DocumentStatus.AT_SUBCONTRACTOR)
                .fromRecipient(RecipientType.INTERNAL_REVIEWER)
                .toRecipient(RecipientType.SUBCONTRACTOR)
                .changedBy("admin")
                .reason("reissue")
                .build();
        document1.addStatusChangeEvent(event0);
        document1.addStatusChangeEvent(event1);
        // when
        documentRepositoryAdapter.deleteAll();
        Document savedDocument1WithHistory = documentRepositoryAdapter.save(document1);
        // then
        Assertions.assertEquals(2,savedDocument1WithHistory.getHistory().size());

        Long idSavedEvent0 = savedDocument1WithHistory.getHistory().get(0).getId();
        Assertions.assertTrue(idSavedEvent0!=null && idSavedEvent0>0);
        Assertions.assertEquals(DocumentStatus.AT_USER,savedDocument1WithHistory.getHistory().get(0).getFromStatus());
        Assertions.assertEquals(DocumentStatus.AT_SUBCONTRACTOR,savedDocument1WithHistory.getHistory().get(0).getToStatus());
        Assertions.assertEquals(RecipientType.INTERNAL_REVIEWER,savedDocument1WithHistory.getHistory().get(0).getFromRecipient());
        Assertions.assertEquals(RecipientType.SUBCONTRACTOR,savedDocument1WithHistory.getHistory().get(0).getToRecipient());
        Assertions.assertEquals("admin",savedDocument1WithHistory.getHistory().get(0).getChangedBy());
        Assertions.assertEquals("issue",savedDocument1WithHistory.getHistory().get(0).getReason());

        Long idSavedEvent1 = savedDocument1WithHistory.getHistory().get(1).getId();
        Assertions.assertTrue(idSavedEvent1!=null && idSavedEvent1>0 && idSavedEvent1>idSavedEvent0);
    }

    @Test
    public void shouldFindDocumentById() {
        Integer documentId = savedDocument1.getDocumentId();
        Optional<Document> foundDocument = documentRepositoryAdapter.findByDocumentId(savedDocument1.getDocumentId());
        Assertions.assertEquals(documentId,foundDocument.get().getDocumentId());
    }

    @Test
    public void shouldReturnEmptyWhenNotFound() {
        PageRequest pageRequest = new PageRequest(0,documentRepositoryAdapter.count());
        List<Document> listDocuments = documentRepositoryAdapter.findAll(pageRequest).content();
        List<Integer> listOfIds = listDocuments.stream()
                .map(Document::getDocumentId)
                .toList();
        Integer maxId = Collections.max(listOfIds);
        Integer maxIdPlusOne = maxId + 1;
        boolean ifExistsMaxIdPlusOne = documentRepositoryAdapter.ifExistsByDocumentId(maxIdPlusOne);
        Assertions.assertFalse(ifExistsMaxIdPlusOne);
    }

    @Test
    public void shouldFindByProjectId() {
        Integer projectId = savedDocument1.getProjectId();
        PageRequest pageRequest = new PageRequest(0,100);
        List<Document> listByProjectId = documentRepositoryAdapter.findByProjectId(projectId,pageRequest).content();
        System.out.println("projectId:" + projectId);
        System.out.println("listByProjectId:" + listByProjectId);
        boolean listContainsProjectId1 = listByProjectId.contains(savedDocument1);
        Assertions.assertTrue(listContainsProjectId1);
    }

    @Test
    public void shouldDeleteDocument() {
        Integer documentId = savedDocument1.getDocumentId();
        Assertions.assertTrue(documentRepositoryAdapter.ifExistsByDocumentId(documentId));
        documentRepositoryAdapter.deleteByDocumentId(documentId);
        Assertions.assertFalse(documentRepositoryAdapter.ifExistsByDocumentId(documentId));
    }

    @Test
    public void checkFindAll() {
        //given
        int totalNumberOfElements = 25;
        int pageNumber = 0;
        int pageSize = 10;
        int numberOfPages = (int) Math.ceil((double) totalNumberOfElements / pageSize);

        PageRequest pageRequest = new PageRequest(pageNumber,pageSize);

        documentRepositoryAdapter.deleteAll();
        for (int i = 0; i < totalNumberOfElements; i++) {
            documentRepositoryAdapter.save(document1);
        }

        //when
        PageResult<Document> pageResult = documentRepositoryAdapter.findAll(pageRequest);

        //then
        Assertions.assertEquals(pageSize,pageResult.content().size());
        Assertions.assertEquals(pageNumber,pageResult.page());
        Assertions.assertEquals(pageSize,pageResult.size());
        Assertions.assertEquals(totalNumberOfElements,pageResult.totalElements());
        Assertions.assertEquals(numberOfPages,pageResult.totalPages());

    }

    @Test
    public void checkDeleteAll() {
        documentRepositoryAdapter.deleteAll();
        PageRequest pageRequest = new PageRequest(0,documentRepositoryAdapter.count()+1);
        int size = documentRepositoryAdapter.findAll(pageRequest).content().size();
        Assertions.assertEquals(0,size);

    }

    @Test
    public void checkCount() {
        Assertions.assertEquals(2,documentRepositoryAdapter.count());
    }








}
