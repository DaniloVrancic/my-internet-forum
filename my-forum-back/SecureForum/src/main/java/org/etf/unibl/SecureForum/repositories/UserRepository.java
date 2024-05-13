package org.etf.unibl.SecureForum.repositories;

import org.etf.unibl.SecureForum.model.enums.UserType;
import org.etf.unibl.SecureForum.model.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>, JpaSpecificationExecutor<UserEntity> {

    Optional<UserEntity> findByUsernameIs(String username);
    Optional<UserEntity> findByEmailIs(String email);

    List<UserEntity> findByTypeIs(UserType type);
}
