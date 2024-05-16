package org.etf.unibl.SecureForum.service;

import org.etf.unibl.SecureForum.base.CrudService;
import org.etf.unibl.SecureForum.model.dto.Permission;
import org.etf.unibl.SecureForum.model.requests.PermissionsRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface PermissionsService extends CrudService<Integer> {

    Permission addPermission(PermissionsRequest request);
}
