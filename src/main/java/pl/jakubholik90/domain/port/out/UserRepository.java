package pl.jakubholik90.domain.port.out;

import pl.jakubholik90.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    public User save(User user);

    public Optional<User> findById(Long userId);

    public Optional<User> findByEmail(String email);

    public void deleteAll();
}
