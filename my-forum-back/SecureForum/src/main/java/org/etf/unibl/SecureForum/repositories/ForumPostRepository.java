package org.etf.unibl.SecureForum.repositories;

import org.etf.unibl.SecureForum.model.ForumPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.sql.Timestamp;
import java.util.List;

public interface ForumPostRepository extends JpaRepository<ForumPostEntity, Integer>, JpaSpecificationExecutor<ForumPostEntity> {

    List<ForumPostEntity> findAllByPostCreator_Id(Integer id);
    List<ForumPostEntity> findAllByPostedAtBefore(Timestamp time);
    List<ForumPostEntity> findAllByPostedAtAfter(Timestamp time);

}