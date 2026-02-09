package pl.jakubholik90.adapter.out.persistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.JpaRepository;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.jakubholik90.adapter.out.persistence.jpa.DocumentJpaRepository;
import pl.jakubholik90.adapter.out.persistence.jpa.DocumentMapper;
import pl.jakubholik90.domain.model.Document;

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

    private Document mockedDocument1;

    private Document mockedDocument2;

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
        mockedDocument1 = Mockito.mock(Document.class);
        mockedDocument2 = Mockito.mock(Document.class);
        documentRepositoryAdapter.save(mockedDocument1);
        documentRepositoryAdapter.save(mockedDocument2);
    }

    @Test
    public void shouldSaveDocument() {
        System.out.println("mockedDocument.getDocumentId():" + mockedDocument1.getDocumentId());
        boolean b = documentRepositoryAdapter.ifExistsByDocumentId(mockedDocument1.getDocumentId());
        Assertions.assertTrue(b);
    }

    @Test
    public void shouldFindDocumentById() {
        Optional<Document> foundDocument = documentRepositoryAdapter.findByDocumentId(mockedDocument1.getDocumentId());
        Assertions.assertEquals(mockedDocument1.getDocumentId(),foundDocument.get().getDocumentId());
    }

    @Test
    public void shouldReturnEmptyWhenNotFound() {
        Optional<Document> foundDocument = documentRepositoryAdapter.findByDocumentId(mockedDocument1.getDocumentId() + 1);
        Assertions.assertTrue(foundDocument.isEmpty());
    }

    @Test
    public void shouldFindByProjectId() {
        when(mockedDocument1.getProjectId()).thenReturn(1);
        when(mockedDocument2.getProjectId()).thenReturn(2);
        List<Document> listByProjectId = documentRepositoryAdapter.findByProjectId(1);
        System.out.println("listByProjectId:" + listByProjectId);
        boolean listContainsProjectId1 = listByProjectId.contains(mockedDocument1);
        Assertions.assertTrue(listContainsProjectId1);
    }

    @Test
    public void shouldDeleteDocument() {
        documentRepositoryAdapter.deleteByDocumentId(mockedDocument1.getDocumentId());
        boolean empty = documentRepositoryAdapter.findByDocumentId(mockedDocument1.getDocumentId()).isEmpty();
        Assertions.assertTrue(empty);
    }

    @Test
    public void checkIfExistsById() {
        boolean ifExists = documentRepositoryAdapter.ifExistsByDocumentId(mockedDocument1.getDocumentId());
        Assertions.assertTrue(ifExists);
    }

    @Test
    public void shouldUpdateDocument() {

    }




}
