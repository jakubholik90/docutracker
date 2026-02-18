package pl.jakubholik90.adapter.out.persistence.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.jakubholik90.adapter.out.persistence.jpa.entity.DocumentEntity;
import pl.jakubholik90.domain.model.DocumentStatus;

import java.util.List;

public interface DocumentJpaRepository extends JpaRepository<DocumentEntity, Integer> {

    Page<DocumentEntity> findByProjectId(Integer projectId, Pageable pageable);

    List<DocumentEntity> findByStatus(DocumentStatus documentStatus);

    List<DocumentEntity> findByProjectIdAndStatus(Integer projectId, DocumentStatus documentStatus);

}
