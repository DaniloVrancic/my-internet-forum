package org.etf.unibl.SecureForum.service.impl;

import org.etf.unibl.SecureForum.base.CrudJpaService;
import org.etf.unibl.SecureForum.model.entities.PermissionsEntity;
import org.etf.unibl.SecureForum.repositories.PermissionsRepository;
import org.etf.unibl.SecureForum.repositories.UserRepository;
import org.etf.unibl.SecureForum.service.PermissionsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
