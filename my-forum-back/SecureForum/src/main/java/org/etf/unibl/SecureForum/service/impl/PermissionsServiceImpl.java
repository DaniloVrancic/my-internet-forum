package org.etf.unibl.SecureForum.service.impl;

import jakarta.transaction.Transactional;
import org.etf.unibl.SecureForum.base.CrudJpaService;
import org.etf.unibl.SecureForum.exceptions.NotFoundException;
import org.etf.unibl.SecureForum.model.dto.Permission;
import org.etf.unibl.SecureForum.model.entities.PermissionsEntity;
import org.etf.unibl.SecureForum.model.entities.TopicEntity;
import org.etf.unibl.SecureForum.model.entities.UserEntity;
import org.etf.unibl.SecureForum.model.requests.PermissionsRequest;
import org.etf.unibl.SecureForum.repositories.PermissionsRepository;
import org.etf.unibl.SecureForum.repositories.TopicRepository;
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
    private final TopicRepository topicRepository;


    @Autowired
    public PermissionsServiceImpl(PermissionsRepository permissionsRepository, ModelMapper modelMapper,
                                  UserRepository userRepository,
                                  TopicRepository topicRepository) {
        super(permissionsRepository, modelMapper, PermissionsEntity.class);

        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.permissionsRepository = permissionsRepository;
        this.topicRepository = topicRepository;
    }

    @Transactional
    public List<Permission> findAllByUserId(Integer user_id)
    {
        List<PermissionsEntity> allFoundEntities = permissionsRepository.findAllByReferencedUser_Id(user_id);

        List<Permission> allFilteredPermissions = new ArrayList<>();

        for(PermissionsEntity entity : allFoundEntities)
        {
            allFilteredPermissions.add(mapPermissionEntityToPermission(entity));
        }

        return allFilteredPermissions;
    }

    @Transactional
    public Permission addPermission(PermissionsRequest request){
        PermissionsEntity entityToAdd = new PermissionsEntity();

        UserEntity tempUser = new UserEntity();
        tempUser.setId(request.getUser_id());
        entityToAdd.setReferencedUser(tempUser);

        TopicEntity tempTopic = topicRepository.findById(request.getTopic_id()).orElseThrow(NotFoundException::new);
        entityToAdd.setTopic(tempTopic);

        entityToAdd.setPermission(request.getType());

        PermissionsEntity savedPermissionEntity = permissionsRepository.save(entityToAdd);


        return mapPermissionEntityToPermission(savedPermissionEntity);
    }

    @Transactional
    public Permission deletePermissionById(Integer id)
    {
        PermissionsEntity foundPermissionEntity = permissionsRepository.findById(id).orElseThrow(NotFoundException::new);
        permissionsRepository.deleteById(id);
        return mapPermissionEntityToPermission(foundPermissionEntity);
    }

    @Transactional
    public List<Permission> deleteAllPermissionForUserId(Integer id){
        List<Permission> foundPermissions = new ArrayList<>();

        List<PermissionsEntity> foundEntities = permissionsRepository.findAllByReferencedUser_Id(id);

        foundEntities.forEach(foundEntity -> {

            foundPermissions.add(mapPermissionEntityToPermission(foundEntity));
        });

        permissionsRepository.deleteAll(foundEntities);
        return foundPermissions;
    }

    @Override
    public List<String> findAllByUserIdAndTopicId(Integer userId, Integer topicId){
        List<PermissionsEntity> foundEntities = permissionsRepository.findByReferencedUser_IdAndTopic_Id(userId, topicId);
        List<String> permissionTypesForTopic = new ArrayList<>();

        for(PermissionsEntity foundEntity : foundEntities){
            permissionTypesForTopic.add(foundEntity.getPermission().getValue().toUpperCase());
        }

        return permissionTypesForTopic;
    }

    private Permission mapPermissionEntityToPermission(PermissionsEntity entity){
        Permission permissionToReturn = new Permission();
        permissionToReturn.setId(entity.getId());
        permissionToReturn.setTopic_id(entity.getId());
        permissionToReturn.setTopic_name(entity.getTopic().getName());
        permissionToReturn.setType(entity.getPermission());
        permissionToReturn.setUser_id(entity.getReferencedUser().getId());

        return permissionToReturn;
    }

    private String sanitize(String value) {
        if (value == null) {
            return null;
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
