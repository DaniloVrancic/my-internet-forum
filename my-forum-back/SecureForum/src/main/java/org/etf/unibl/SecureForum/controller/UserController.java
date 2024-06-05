package org.etf.unibl.SecureForum.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.etf.unibl.SecureForum.exceptions.BadRequestException;
import org.etf.unibl.SecureForum.exceptions.DuplicateEntryException;
import org.etf.unibl.SecureForum.exceptions.NotFoundException;
import org.etf.unibl.SecureForum.model.dto.User;
import org.etf.unibl.SecureForum.model.dto.VerificationCodeEmailMessage;
import org.etf.unibl.SecureForum.model.entities.CodeVerificationEntity;
import org.etf.unibl.SecureForum.model.entities.UserEntity;
import org.etf.unibl.SecureForum.model.requests.*;
import org.etf.unibl.SecureForum.repositories.CodeVerificationRepository;
import org.etf.unibl.SecureForum.repositories.UserRepository;
import org.etf.unibl.SecureForum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final CodeVerificationRepository codeVerificationRepository;

    @Autowired
    public UserController(UserService userService,
                          UserRepository userRepository,
                          CodeVerificationRepository codeVerificationRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.codeVerificationRepository = codeVerificationRepository;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAll(){
        return userService.findAll(User.class);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User findUserById(@PathVariable Integer id) throws NotFoundException
    { return userService.findById(id, User.class);}

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@Valid @RequestBody UserInsertRequest request){


        UserEntity entityToInsert = new UserEntity();
        entityToInsert.setUsername(request.getUsername());
        entityToInsert.setEmail(request.getEmail());
        entityToInsert.setStatus(request.getStatus());
        entityToInsert.setType(request.getType());

        if(request.getCreateTime() == null)
        {
            request.setCreateTime(Timestamp.from(Instant.now()));
        }
        entityToInsert.setOauth_account(false);

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

    @PutMapping("/update")
    @Transactional
    public User updateUser(@RequestBody UserEntity userData) {

        UserEntity foundUser = userRepository.findById(userData.getId()).orElseThrow(NotFoundException::new);

        /*  Setting all the fields that need to be updated */
        if(!userData.getUsername().isEmpty()){
            foundUser.setUsername(userData.getUsername());
        }
        if(!userData.getEmail().isEmpty()){
            foundUser.setEmail(userData.getEmail());
        }
        if(userData.getType() != null)
        {
            foundUser.setType(userData.getType());
        }
        if(userData.getCreateTime() != null){
            foundUser.setCreateTime(userData.getCreateTime());
        }
        if(userData.getStatus() != null){
            foundUser.setStatus(userData.getStatus());
        }

        if(userData.getPassword() != null && !userData.getPassword().isEmpty()) //if a new password was set, then hash it again before placing in database
        {
            PasswordEncoder encoder = getPasswordEncoder();
            foundUser.setPassword(encoder.encode(userData.getPassword()));
        }
        User userToReturn = null;
        try{
            userToReturn = userService.update(foundUser.getId(), foundUser, User.class);
        }
        catch(DataIntegrityViolationException ex)
        {
            throw new DuplicateEntryException();
        }
        catch (Exception ex){
            throw new BadRequestException();
        }

        return userToReturn;
    }

    @PutMapping("/change-privileges")
    @Transactional
    public User changeTypeUser(@RequestBody UpdateUserPrivilegesRequest request) {
        try{
            return  userService.changePrivileges(request);
        }
        catch (Exception ex){
            throw new BadRequestException();
        }
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUserById(@PathVariable("id") Integer user_Id)
    {
        try
        {
            userService.delete(user_Id);
        }
        catch(Exception ex)
        {
            throw new BadRequestException();
        }

        return "Successfully deleted User (ID = " + user_Id + ")";
    }

    @DeleteMapping("/delete-username/{username}")
    public String deleteUserByUsername(@PathVariable("username") String username)
    {
        try
        {
            UserEntity foundUser = userRepository.findByUsernameIs(username).orElseThrow(NotFoundException::new);
            userService.delete(foundUser.getId());
        }
        catch(Exception ex)
        {
            throw new BadRequestException();
        }

        return "Successfully deleted User (Username = " + username + ")";
    }


    private PasswordEncoder getPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }


}
