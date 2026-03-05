package pl.jakubholik90.adapter.out.persistence.jpa;

import org.springframework.stereotype.Repository;
import pl.jakubholik90.adapter.out.persistence.jpa.entity.UserEntity;
import pl.jakubholik90.domain.model.User;
import pl.jakubholik90.domain.port.out.UserRepository;

import java.util.Optional;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    public UserRepositoryAdapter(UserJpaRepository userJpaRepository, UserMapper userMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = userMapper.mapToUserEntity(user);
        UserEntity savedEntity = userJpaRepository.save(userEntity);
        User savedUser = userMapper.mapToUser(savedEntity);
        return savedUser;
    }

    @Override
    public Optional<User> findById(Long userId) {
        Optional<UserEntity> optionalUserEntity = userJpaRepository.findById(userId);
        if (optionalUserEntity.isEmpty()) {
            return Optional.empty();
        } else {
            UserEntity userEntity = optionalUserEntity.get();
            User user = userMapper.mapToUser(userEntity);
            return Optional.of(user);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> optionalUserEntity = userJpaRepository.findByEmail(email);
        if (optionalUserEntity.isEmpty()) {
            return Optional.empty();
        } else {
            UserEntity userEntity = optionalUserEntity.get();
            User user = userMapper.mapToUser(userEntity);
            return Optional.of(user);
        }
    }

    @Override
    public void deleteAll() {
        userJpaRepository.deleteAll();
    }
}
