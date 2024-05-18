package org.etf.unibl.SecureForum.service;

import org.etf.unibl.SecureForum.base.CrudService;
import org.etf.unibl.SecureForum.model.dto.User;
import org.etf.unibl.SecureForum.model.entities.CodeVerificationEntity;
import org.etf.unibl.SecureForum.model.entities.UserEntity;
import org.etf.unibl.SecureForum.model.requests.*;

import java.util.List;

public interface UserService extends CrudService<Integer> {

    User signUp(SignUpRequest request);

    User login(LoginRequest request);

    User changeStatus(ChangeStatusRequest request);

    User changeRole(ChangeTypeRequest request);

    List<CodeVerificationEntity> getAllCodesForUser(Integer userId);

    void generateNewVerificationCode(UserEntity savedUser);

}
