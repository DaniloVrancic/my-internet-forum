package org.etf.unibl.SecureForum.repositories;

import org.etf.unibl.SecureForum.model.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer>, JpaSpecificationExecutor<CommentEntity> {

    List<CommentEntity> findAllByReferencedUser_id(Integer userId);
    List<CommentEntity> findAllByReferencedPost_Id(Integer postId);
}
