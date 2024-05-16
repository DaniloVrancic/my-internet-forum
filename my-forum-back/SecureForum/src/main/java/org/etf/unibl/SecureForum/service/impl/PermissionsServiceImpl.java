package org.etf.unibl.SecureForum.service.impl;

import jakarta.transaction.Transactional;
import org.etf.unibl.SecureForum.base.CrudJpaService;
import org.etf.unibl.SecureForum.model.dto.Permission;
import org.etf.unibl.SecureForum.model.entities.PermissionsEntity;
import org.etf.unibl.SecureForum.model.entities.TopicEntity;
import org.etf.unibl.SecureForum.model.entities.UserEntity;
import org.etf.unibl.SecureForum.model.requests.PermissionsRequest;
import org.etf.unibl.SecureForum.repositories.PermissionsRepository;
import org.etf.unibl.SecureForum.repositories.UserRepository;
import org.etf.unibl.SecureForum.service.PermissionsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PermissionsServiceImpl extends CrudJpaService<PermissionsEntity, Integer> implements PermissionsService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PermissionsRepository permissionsRepository;


    @Autowired
    public PermissionsServiceImpl(PermissionsRepository permissionsRepository, ModelMapper modelMapper, Class<PermissionsEntity> entityClass, UserRepository userRepository) {
        super(permissionsRepository, modelMapper, entityClass);

        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.permissionsRepository = permissionsRepository;
    }

    @Transactional
    public Permission addPermission(PermissionsRequest request){
        PermissionsEntity entityToAdd = new PermissionsEntity();

        UserEntity tempUser = new UserEntity();
        tempUser.setId(request.getUser_id());
        entityToAdd.setReferencedUser(tempUser);

        TopicEntity tempTopic = new TopicEntity();
        tempTopic.setId(request.getTopic_id());
        entityToAdd.setTopic(tempTopic);

        entityToAdd.setPermission(request.getType());

        Permission savedPermission = insert(entityToAdd,Permission.class);

        return savedPermission;
    }

    @Transactional
    public Permission deletePermissionById(Integer id)
    {
        Permission foundPermission = findById(id, Permission.class);
        delete(id);
        return foundPermission;
    }

    @Transactional
    public List<Permission> deleteAllPermissionForUserId(Integer id){
        List<Permission> foundPermissions = new ArrayList<>();

        List<PermissionsEntity> foundEntities = permissionsRepository.findAllByReferencedUser_Id(id);

        foundEntities.forEach(foundEntity -> {
            Permission newPermission = new Permission();
            newPermission.setId(foundEntity.getId());
            newPermission.setType(foundEntity.getPermission());
            newPermission.setUser_id(id);
            newPermission.setTopic_id(foundEntity.getTopic().getId());
            foundPermissions.add(newPermission);
        });

        deleteAllPermissionForUserId(id);
        return foundPermissions;
    }
}
