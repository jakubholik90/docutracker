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
import java.util.ArrayList;
import java.util.Collections;
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

//    @Autowired
//    private DocumentJpaRepository documentJpaRepository;


    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

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
    public void shouldFindDocumentById() {
        Integer documentId = savedDocument1.getDocumentId();
        Optional<Document> foundDocument = documentRepositoryAdapter.findByDocumentId(savedDocument1.getDocumentId());
        Assertions.assertEquals(documentId,foundDocument.get().getDocumentId());
    }

    @Test
    public void shouldReturnEmptyWhenNotFound() {
        List<Document> listDocuments = documentRepositoryAdapter.findAll();
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
        List<Document> listByProjectId = documentRepositoryAdapter.findByProjectId(projectId);
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
        List<Document> allFoundDocuments = documentRepositoryAdapter.findAll();
        List<Document> assertionList = new ArrayList<>();
        assertionList.add(savedDocument1);
        assertionList.add(savedDocument2);
        System.out.println("allFoundDocuments: " + allFoundDocuments);
        System.out.println("assertionList: " + assertionList);
        Assertions.assertEquals(assertionList, allFoundDocuments);
    }

    @Test
    public void checkDeleteAll() {
        documentRepositoryAdapter.deleteAll();
        int size = documentRepositoryAdapter.findAll().size();
        Assertions.assertEquals(0,size);

    }








}
