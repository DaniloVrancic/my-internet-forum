package org.etf.unibl.SecureForum.repositories;

import org.etf.unibl.SecureForum.model.entities.OauthAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OauthAccountRepository extends JpaRepository<OauthAccountEntity, Integer>, JpaSpecificationExecutor<OauthAccountEntity> {

    List<OauthAccountEntity> findAllByOauthProvider(String oAuthProviderName);
    List<OauthAccountEntity> findByReferencedUser_Id(Integer id);
}
