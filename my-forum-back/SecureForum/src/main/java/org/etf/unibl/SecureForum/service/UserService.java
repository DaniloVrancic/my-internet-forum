package org.etf.unibl.SecureForum.service;

import org.etf.unibl.SecureForum.base.CrudService;
import org.etf.unibl.SecureForum.model.dto.User;
import org.etf.unibl.SecureForum.model.requests.*;

public interface UserService extends CrudService<Integer> {

    User signUp(SignUpRequest request);

    User changeStatus(ChangeStatusRequest request);

    User changeRole(ChangeTypeRequest request);


}
