package pl.jakubholik90.adapter.out.persistence.jpa;

import org.springframework.stereotype.Repository;
import pl.jakubholik90.domain.model.GmailConnection;
import pl.jakubholik90.domain.port.out.GmailConnectionRepository;

import java.util.Optional;

@Repository
public class GmailConnectionRepositoryAdapter implements GmailConnectionRepository {

    private GmailConnectionJpaRepository gmailConnectionJpaRepository;
    private GmailConnectionMapper gmailConnectionMapper;

    public GmailConnectionRepositoryAdapter(GmailConnectionJpaRepository gmailConnectionJpaRepository, GmailConnectionMapper gmailConnectionMapper) {
        this.gmailConnectionJpaRepository = gmailConnectionJpaRepository;
        this.gmailConnectionMapper = gmailConnectionMapper;
    }

    @Override
    public GmailConnection save(GmailConnection connection) {
        return null;
    }

    @Override
    public Optional<GmailConnection> findByUserId(Long userId) {
        return Optional.empty();
    }

    @Override
    public void delete(GmailConnection connection) {

    }

    @Override
    public void deleteAll() {
        gmailConnectionJpaRepository.deleteAll();
    }
}
