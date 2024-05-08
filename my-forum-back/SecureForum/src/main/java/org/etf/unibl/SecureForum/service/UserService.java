package org.etf.unibl.SecureForum.service;

import org.etf.unibl.SecureForum.base.CrudService;
import org.etf.unibl.SecureForum.model.entities.UserEntity;
import org.etf.unibl.SecureForum.model.requests.ChangeRoleRequest;
import org.etf.unibl.SecureForum.model.requests.ChangeStatusRequest;
import org.etf.unibl.SecureForum.model.requests.SignUpRequest;
import org.etf.unibl.SecureForum.model.requests.UserUpdateRequest;
import org.springframework.data.repository.core.CrudMethods;

public interface UserService extends CrudService<Integer> {

    void signUp(SignUpRequest request);

    void changeStatus(Integer userId, ChangeStatusRequest request);

    void changeRole(Integer userId, ChangeRoleRequest request);

    UserEntity update(Integer id, UserUpdateRequest request);

}
