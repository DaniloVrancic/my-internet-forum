package org.etf.unibl.SecureForum.controller;

import jakarta.persistence.EntityExistsException;
import org.etf.unibl.SecureForum.model.dto.Permission;
import org.etf.unibl.SecureForum.model.entities.PermissionsEntity;
import org.etf.unibl.SecureForum.model.enums.PermissionType;
import org.etf.unibl.SecureForum.model.requests.PermissionsRequest;
import org.etf.unibl.SecureForum.repositories.PermissionsRepository;
import org.etf.unibl.SecureForum.service.PermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/permissions")
@Validated
public class PermissionsController {

    private final PermissionsService permissionsService;
    private final PermissionsRepository permissionsRepository;

    @Autowired
    public PermissionsController(PermissionsService permissionsService,
                                 PermissionsRepository permissionsRepository){
        this.permissionsService = permissionsService;
        this.permissionsRepository = permissionsRepository;
    }

    @GetMapping
    public List<Permission> findAllPermissions()
    {
        List<PermissionsEntity> allEntities = permissionsRepository.findAll();
        List<Permission> mappedEntities = new ArrayList<>();

        for(PermissionsEntity currentEntity : allEntities){
            Permission newPermission = new Permission();
            newPermission.setId(currentEntity.getId());
            newPermission.setUser_id(currentEntity.getReferencedUser().getId());
            newPermission.setTopic_id(currentEntity.getTopic().getId());
            newPermission.setTopic_name(currentEntity.getTopic().getName());
            newPermission.setType(currentEntity.getPermission());
            mappedEntities.add(newPermission);
        }

        return mappedEntities;
    }

    @GetMapping("/{user_id}")
    public List<Permission> findAllPermissionsForUserId(@PathVariable("user_id") Integer user_id){
        return permissionsService.findAllByUserId(user_id);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Permission addPermission(@RequestBody PermissionsRequest request)
    {
        Permission permissionToReturn = new Permission();
        PermissionsEntity foundEntity = null;
        if((foundEntity = permissionsRepository.findByReferencedUser_IdAndTopic_IdAndAndPermission(request.getUser_id(), request.getTopic_id(), request.getType())) == null)
        {
            return permissionsService.addPermission(request);
        }
        else{
            permissionToReturn.setId(foundEntity.getId());
            permissionToReturn.setType(foundEntity.getPermission());
            permissionToReturn.setUser_id(foundEntity.getReferencedUser().getId());
            permissionToReturn.setTopic_id(foundEntity.getTopic().getId());
            permissionToReturn.setTopic_name(foundEntity.getTopic().getName());
            return permissionToReturn;
        }
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
