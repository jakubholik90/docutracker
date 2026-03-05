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
import pl.jakubholik90.adapter.out.persistence.jpa.UserMapper;
import pl.jakubholik90.adapter.out.persistence.jpa.UserRepositoryAdapter;
import pl.jakubholik90.domain.model.User;
import java.util.Optional;

@DataJpaTest
@Testcontainers //handling test containers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({UserRepositoryAdapter.class, UserMapper.class})
public class UserRepositoryAdapterTest {

    @Autowired
    UserRepositoryAdapter userRepositoryAdapter;

    private User user1; // new user, before saving by JPA, with Id==null
    private User savedUser1; // saved user, with Id!=null

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
        userRepositoryAdapter.deleteAll();
        user1 = User.builder()
                .email("testEmail1")
                .name("testName1")
                .build();
        savedUser1 = userRepositoryAdapter.save(user1); // JPa creates userId here
    }

    @Test
    public void checkFindByIdWhenIdExists() {
        User foundUser = userRepositoryAdapter.findById(savedUser1.getId()).get();
        Assertions.assertTrue(foundUser.equals(savedUser1));
    }

    @Test
    public void checkFindByIdWhenIdNotExist() {
        boolean foundUserIsEmpty = userRepositoryAdapter.findById(100L).isEmpty();
        Assertions.assertTrue(foundUserIsEmpty);
    }

    @Test
    public void shouldSaveUser() {
        User userToSave = User.builder()
                .name("testName2")
                .email("testEmail2")
                .build();
        userRepositoryAdapter.save(userToSave);
        User userFound =  userRepositoryAdapter.findByEmail(userToSave.getEmail()).get();
        Assertions.assertNotNull(userFound.getId());
        Assertions.assertEquals(userToSave.getEmail(),userFound.getEmail());
        Assertions.assertEquals(userToSave.getName(),userFound.getName());
    }

    @Test
    public void checkFindByEmailWhenEmailExists() {
        User foundUser = userRepositoryAdapter.findByEmail(savedUser1.getEmail()).get();
        Assertions.assertTrue(foundUser.equals(savedUser1));
    }

    @Test
    public void checkFindByEmailWhenEmailNotExist() {
        boolean foundUserIsEmpty = userRepositoryAdapter.findByEmail("notExistingEmail").isEmpty();
        Assertions.assertTrue(foundUserIsEmpty);
    }


}
