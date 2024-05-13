package org.etf.unibl.SecureForum.repositories;

import org.etf.unibl.SecureForum.model.entities.PermissionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PermissionsRepository extends JpaRepository<PermissionsEntity, Integer>, JpaSpecificationExecutor<PermissionsEntity> {

    List<PermissionsEntity> findAllByReferencedUser_Id(Integer userId);
}
