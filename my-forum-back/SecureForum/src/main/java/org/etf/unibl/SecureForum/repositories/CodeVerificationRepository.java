package org.etf.unibl.SecureForum.repositories;

import jakarta.transaction.Transactional;
import org.etf.unibl.SecureForum.model.entities.CodeVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CodeVerificationRepository extends JpaRepository<CodeVerificationEntity, Integer>, JpaSpecificationExecutor<CodeVerificationEntity> {

    @Transactional
    List<CodeVerificationEntity> findAllByReferencedUser_Id(Integer userId);

    CodeVerificationEntity findByReferencedUser_Id(Integer userId);

    @Transactional
    Integer deleteCodeVerificationEntitiesByReferencedUserId(Integer userId);
}
