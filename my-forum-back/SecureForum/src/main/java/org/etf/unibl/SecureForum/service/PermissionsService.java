package org.etf.unibl.SecureForum.service;

import org.etf.unibl.SecureForum.base.CrudService;
import org.etf.unibl.SecureForum.model.dto.Permission;
import org.etf.unibl.SecureForum.model.requests.PermissionsRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public interface PermissionsService extends CrudService<Integer> {

    Permission addPermission(PermissionsRequest request);

    List<Permission> findAllByUserId(Integer user_id);

    List<String> findAllByUserIdAndTopicId(Integer userId, Integer topicId);
    Permission deletePermissionById(Integer id);
    List<Permission> deleteAllPermissionForUserId(Integer id);


}
