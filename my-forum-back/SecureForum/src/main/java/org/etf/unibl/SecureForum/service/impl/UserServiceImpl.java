package org.etf.unibl.SecureForum.service.impl;

import jakarta.transaction.Transactional;
import org.etf.unibl.SecureForum.base.CrudJpaService;
import org.etf.unibl.SecureForum.model.entities.UserEntity;
import org.etf.unibl.SecureForum.model.requests.ChangeRoleRequest;
import org.etf.unibl.SecureForum.model.requests.ChangeStatusRequest;
import org.etf.unibl.SecureForum.model.requests.SignUpRequest;
import org.etf.unibl.SecureForum.model.requests.UserUpdateRequest;
import org.etf.unibl.SecureForum.repositories.UserRepository;
import org.etf.unibl.SecureForum.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class UserServiceImpl extends CrudJpaService<UserEntity, Integer> implements UserService {

    private final ModelMapper modelMapper;
    private final UserRepository repository;

    public UserServiceImpl(ModelMapper modelMapper, UserRepository repository)
    {
        super(repository, modelMapper, UserEntity.class);
        this.modelMapper = modelMapper;
        this.repository = repository;
    }


    @Override
    public void signUp(SignUpRequest request) {
        //TODO: IMPLEMENT METHOD
    }

    @Override
    public void changeStatus(Integer userId, ChangeStatusRequest request) {
        //TODO: IMPLEMENT METHOD
    }

    @Override
    public void changeRole(Integer userId, ChangeRoleRequest request) {
        //TODO: IMPLEMENT METHOD
    }

    @Override
    public UserEntity update(Integer id, UserUpdateRequest request) {
        //TODO: IMPLEMENT METHOD
        return null;
    }
}
