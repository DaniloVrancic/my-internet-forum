package org.etf.unibl.SecureForum.controller;

import org.etf.unibl.SecureForum.model.dto.Permission;
import org.etf.unibl.SecureForum.model.entities.PermissionsEntity;
import org.etf.unibl.SecureForum.model.requests.PermissionsRequest;
import org.etf.unibl.SecureForum.service.PermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@Validated
public class PermissionsController {

    private final PermissionsService permissionsService;

    @Autowired
    public PermissionsController(PermissionsService permissionsService){
        this.permissionsService = permissionsService;
    }

    @GetMapping
    public List<Permission> findAllPermissions()
    {
        return permissionsService.findAll(Permission.class);
    }

    @GetMapping("/{user_id}")
    public List<Permission> findAllPermissionsForUserId(@PathVariable("user_id") Integer user_id){
        return findAllPermissionsForUserId(user_id);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Permission addPermission(@RequestBody PermissionsRequest request)
    {
        return permissionsService.addPermission(request);
    }

    @DeleteMapping("/delete/{id}")
    public Permission deletePermission(@PathVariable("id") Integer id)
    {
        return permissionsService.deletePermissionById(id);
    }

    @DeleteMapping("/delete_by_user_id/{user_id}")
    public List<Permission> deleteByUserId(@PathVariable("user_id") Integer user_id)
    {
        return permissionsService.deleteAllPermissionForUserId(user_id);
    }
}
