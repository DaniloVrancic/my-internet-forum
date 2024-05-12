package org.etf.unibl.SecureForum.controller;

import org.etf.unibl.SecureForum.exceptions.BadRequestException;
import org.etf.unibl.SecureForum.exceptions.DuplicateEntryException;
import org.etf.unibl.SecureForum.exceptions.NotFoundException;
import org.etf.unibl.SecureForum.model.dto.User;
import org.etf.unibl.SecureForum.model.entities.UserEntity;
import org.etf.unibl.SecureForum.model.requests.SignUpRequest;
import org.etf.unibl.SecureForum.model.requests.UserInsertRequest;
import org.etf.unibl.SecureForum.model.requests.UserUpdateRequest;
import org.etf.unibl.SecureForum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public List<User> findAll(){
        return userService.findAll(User.class);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public User findUserById(@PathVariable Integer id) throws NotFoundException
    { return userService.findById(id, User.class);}

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody SignUpRequest request){


        User newUser = userService.signUp(request);


        return newUser;
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody UserInsertRequest request){


        UserEntity entityToInsert = new UserEntity();
        entityToInsert.setUsername(request.getUsername());
        entityToInsert.setEmail(request.getEmail());
        entityToInsert.setStatus(request.getStatus());
        entityToInsert.setType(request.getType());

        if(request.getCreateTime() == null)
        {
            request.setCreateTime(Timestamp.from(Instant.now()));
        }

        entityToInsert.setCreateTime(request.getCreateTime());

        entityToInsert.setPassword(getPasswordEncoder().encode(request.getPassword())); //Going to hash the password
        User newUser = null;
        try{
            newUser = userService.insert(entityToInsert, User.class);
        }
        catch (DataIntegrityViolationException ex) //Will happen if the username or email is already taken
        {
            throw new DuplicateEntryException();
        }

        return newUser;
    }

    private PasswordEncoder getPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }


}
