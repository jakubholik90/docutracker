package pl.jakubholik90.adapter.out.persistence.jpa;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers //handling test containers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({GmailConnectionRepositoryAdapter.class, GmailConnectionMapper.class})
public class GmailConnectionRepositoryAdapterTest {

    @Autowired
    private GmailConnectionRepositoryAdapter gmailConnectionRepositoryAdapter;



    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @BeforeEach
    public void setUp() {
        gmailConnectionRepositoryAdapter.deleteAll();
    }

    @Test
    public void shouldSaveGmailConnection() {}

    @Test
    public void shouldFindByUserId() {}

    @Test
    public void shouldReturnEmptyWhenUserNotConnected() {}

    @Test
    public void shouldDeleteGmailConnection() {}

    @Test
    public void shouldUpdateExistingConnection() {}


}
