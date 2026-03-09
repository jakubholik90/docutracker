package pl.jakubholik90.domain.port.out;

import pl.jakubholik90.domain.model.GmailConnection;

import java.util.Optional;

public interface GmailConnectionRepository {

    public GmailConnection save(GmailConnection connection);

    public Optional<GmailConnection> findByUserId(Long userId);

    public void delete(GmailConnection connection);

    public void deleteAll();

}
