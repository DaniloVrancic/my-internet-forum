package org.etf.unibl.SecureForum.repositories;

import org.etf.unibl.SecureForum.model.entities.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TopicRepository extends JpaRepository<TopicEntity, Integer>, JpaSpecificationExecutor<TopicEntity> {

    List<TopicEntity> findAllByNameContaining(String namePart);
    List<TopicEntity> findAllByNameEquals(String name);
}
