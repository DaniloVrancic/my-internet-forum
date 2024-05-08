package org.etf.unibl.SecureForum.repositories;

import org.etf.unibl.SecureForum.model.entities.AccessLogsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AccessLogsRepository extends JpaRepository<AccessLogsEntity, Integer>, JpaSpecificationExecutor<AccessLogsEntity> {

    List<AccessLogsEntity> findAll();
    List<AccessLogsEntity> findAllByReferencedUser_Id(Integer userId);
}
