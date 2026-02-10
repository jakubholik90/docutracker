package pl.jakubholik90.adapter.out.persistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.jakubholik90.adapter.out.persistence.jpa.DocumentJpaRepository;
import pl.jakubholik90.adapter.out.persistence.jpa.DocumentMapper;
import pl.jakubholik90.domain.model.Document;
import pl.jakubholik90.domain.model.DocumentStatus;
import pl.jakubholik90.domain.model.RecipientType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

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

    @Autowired
    private DocumentJpaRepository documentJpaRepository;


    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @BeforeEach
    public void setUp() {
        documentJpaRepository.deleteAll();
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
    public void shouldFindDocumentById() {
        Optional<Document> foundDocument = documentRepositoryAdapter.findByDocumentId(document1.getDocumentId());
        Assertions.assertEquals(document1.getDocumentId(),foundDocument.get().getDocumentId());
    }

    @Test
    public void shouldReturnEmptyWhenNotFound() {
        Optional<Document> foundDocument = documentRepositoryAdapter.findByDocumentId(document1.getDocumentId() + 1);
        Assertions.assertTrue(foundDocument.isEmpty());
    }

    @Test
    public void shouldFindByProjectId() {
        when(document1.getProjectId()).thenReturn(1);
        when(document2.getProjectId()).thenReturn(2);
        List<Document> listByProjectId = documentRepositoryAdapter.findByProjectId(1);
        System.out.println("listByProjectId:" + listByProjectId);
        boolean listContainsProjectId1 = listByProjectId.contains(document1);
        Assertions.assertTrue(listContainsProjectId1);
    }

    @Test
    public void shouldDeleteDocument() {
        documentRepositoryAdapter.deleteByDocumentId(document1.getDocumentId());
        boolean empty = documentRepositoryAdapter.findByDocumentId(document1.getDocumentId()).isEmpty();
        Assertions.assertTrue(empty);
    }






}
