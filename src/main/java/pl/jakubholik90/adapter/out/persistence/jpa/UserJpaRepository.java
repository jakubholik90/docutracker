package pl.jakubholik90.adapter.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jakubholik90.adapter.out.persistence.jpa.entity.UserEntity;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByEmail(String email);
}
