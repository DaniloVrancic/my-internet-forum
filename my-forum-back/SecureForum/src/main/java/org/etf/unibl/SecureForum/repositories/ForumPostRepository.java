package org.etf.unibl.SecureForum.repositories;

import org.etf.unibl.SecureForum.model.entities.ForumPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
@Repository
public interface ForumPostRepository extends JpaRepository<ForumPostEntity, Integer>, JpaSpecificationExecutor<ForumPostEntity> {

    List<ForumPostEntity> findAllByPostCreator_Id(Integer id);

    List<ForumPostEntity> findAllByPostTopic_Id(Integer id);
    List<ForumPostEntity> findAllByPostedAtBefore(Timestamp time);
    List<ForumPostEntity> findAllByPostedAtAfter(Timestamp time);

    List<ForumPostEntity> findAllByStatusAndPostTopic_Id(ForumPostEntity.Status status, Integer id);

    List<ForumPostEntity> findAllByStatusIs(ForumPostEntity.Status status);

}
