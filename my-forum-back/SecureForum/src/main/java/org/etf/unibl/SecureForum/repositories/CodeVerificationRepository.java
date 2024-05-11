package org.etf.unibl.SecureForum.repositories;

import org.etf.unibl.SecureForum.model.entities.CodeVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CodeVerificationRepository extends JpaRepository<CodeVerificationEntity, Integer>, JpaSpecificationExecutor<CodeVerificationEntity> {
    List<CodeVerificationEntity> findAllByReferencedUser_Id(Integer userId);
}
