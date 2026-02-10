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
import pl.jakubholik90.domain.model.RecipientType;

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

    private Document document1;

    private Document document2;

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
                .build();
        document2 = Document.builder()
                .currentRecipient(RecipientType.INTERNAL_REVIEWER).
                build();
        documentRepositoryAdapter.save(document1);
        documentRepositoryAdapter.save(document2);
    }

    @Test
    public void shouldSaveDocument() {
        System.out.println("mockedDocument.getDocumentId():" + document1.getDocumentId());
        boolean b = documentRepositoryAdapter.ifExistsByDocumentId(document1.getDocumentId());
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

    @Test
    public void checkIfExistsById() {
        boolean ifExists = documentRepositoryAdapter.ifExistsByDocumentId(document1.getDocumentId());
        Assertions.assertTrue(ifExists);
    }





}
