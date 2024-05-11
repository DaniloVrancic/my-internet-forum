package org.etf.unibl.SecureForum.service.impl;

import jakarta.transaction.Transactional;
import org.etf.unibl.SecureForum.base.CrudJpaService;
import org.etf.unibl.SecureForum.model.dto.User;
import org.etf.unibl.SecureForum.model.entities.UserEntity;
import org.etf.unibl.SecureForum.model.requests.ChangeRoleRequest;
import org.etf.unibl.SecureForum.model.requests.ChangeStatusRequest;
import org.etf.unibl.SecureForum.model.requests.SignUpRequest;
import org.etf.unibl.SecureForum.model.requests.UserUpdateRequest;
import org.etf.unibl.SecureForum.repositories.UserRepository;
import org.etf.unibl.SecureForum.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class UserServiceImpl extends CrudJpaService<UserEntity, Integer> implements UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository)
    {
        super(userRepository, modelMapper, UserEntity.class); //For implementing CRUD operations
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }


    @Override
    public User signUp(SignUpRequest request) {
        UserEntity userForDatabase = new UserEntity();


        PasswordEncoder encoder = getBCryptEncoder();   //Getting encoder for hashing
        userForDatabase.setPassword(encoder.encode(request.getPassword())); //hashing the password for database

        userForDatabase.setUsername(request.getUsername());
        userForDatabase.setEmail(request.getEmail());
        userForDatabase.setType(request.getType());

        if(request.getStatus() != null) //if the request specifies the status, assign that status...
        {
            userForDatabase.setStatus(request.getStatus());
        }
        else{ //or else, by default, assign the Status to REQUESTED upon creating the entity
            userForDatabase.setStatus(UserEntity.Status.REQUESTED);
        }

        UserEntity savedUser = userRepository.save(userForDatabase);
    }

    @Override
    public User changeStatus(Integer userId, ChangeStatusRequest request) {
        //TODO: IMPLEMENT METHOD
    }

    @Override
    public User changeRole(Integer userId, ChangeRoleRequest request) {
        //TODO: IMPLEMENT METHOD
    }

    @Override
    public User update(Integer id, UserUpdateRequest request) {
        //TODO: IMPLEMENT METHOD
        return null;
    }

    private PasswordEncoder getBCryptEncoder(){
        return new BCryptPasswordEncoder();
    }
}
