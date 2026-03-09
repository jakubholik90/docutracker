package pl.jakubholik90.adapter.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jakubholik90.adapter.out.persistence.jpa.entity.GmailConnectionEntity;

import java.util.Optional;

public interface GmailConnectionJpaRepository extends JpaRepository<GmailConnectionEntity,Long> {

    public Optional<GmailConnectionEntity> findByUserId(Long userId);
}


