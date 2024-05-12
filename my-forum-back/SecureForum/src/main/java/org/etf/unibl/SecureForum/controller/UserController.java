package org.etf.unibl.SecureForum.controller;

import org.etf.unibl.SecureForum.exceptions.BadRequestException;
import org.etf.unibl.SecureForum.exceptions.DuplicateEntryException;
import org.etf.unibl.SecureForum.exceptions.NotFoundException;
import org.etf.unibl.SecureForum.model.dto.User;
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
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/users")
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

    @PostMapping("/verify")
    public User verifyUser(@RequestBody VerifyUserRequest request)
    {
        List<CodeVerificationEntity> listOfCodes = userService.getAllCodesForUser(request.getUser_id());
        boolean found = false;

        for(CodeVerificationEntity codeEntity : listOfCodes){
            if(codeEntity.getVerificationCode().trim().equals(request.getVerificationCode().trim()))
            {
                found = true;
                break;
            }
        }

        if(found){ //delete all the codes for this user in database and update the user as verified and return him.
            UserEntity userToVerify = userRepository.findById(request.getUser_id()).orElseThrow(NotFoundException::new);
            userToVerify.setStatus(UserEntity.Status.ACTIVE);
            User savedUser = userService.update(userToVerify.getId(), userToVerify, User.class);
            codeVerificationRepository.deleteCodeVerificationEntitiesByReferencedUserId(request.getUser_id()); //deletes the codes created for this user (multiple just in case if more were created)
            return savedUser;
        }
        else{
            throw new NotFoundException();
        }
    }

    @PutMapping("/update")
    public User updateUser(@RequestBody UserEntity userData) {

        UserEntity foundUser = userRepository.findById(userData.getId()).orElseThrow(NotFoundException::new);

        /*  Setting all the fields that need to be updated */
        if(!userData.getUsername().isEmpty())
        foundUser.setUsername(userData.getUsername());
        if(!userData.getEmail().isEmpty())
        foundUser.setEmail(userData.getEmail());
        if(userData.getType() != null)
        foundUser.setType(userData.getType());
        if(userData.getCreateTime() != null)
        foundUser.setCreateTime(userData.getCreateTime());
        if(userData.getStatus() != null)
        foundUser.setStatus(userData.getStatus());

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

    @PutMapping("/change-type")
    public User changeTypeUser(@RequestBody ChangeTypeRequest request) {

        UserEntity foundUser = userRepository.findById(request.getId()).orElseThrow(NotFoundException::new);

        foundUser.setType(request.getType());


        User userToReturn = null;
        try{
            userToReturn = userService.update(foundUser.getId(), foundUser, User.class);
        }
        catch (Exception ex){
            throw new BadRequestException();
        }

        return userToReturn;
    }

    @PutMapping("/change-status")
    public User changeTypeUser(@RequestBody ChangeStatusRequest request) {

        UserEntity foundUser = userRepository.findById(request.getId()).orElseThrow(NotFoundException::new);

        foundUser.setStatus(request.getStatus());

        User userToReturn = null;
        try{
            userToReturn = userService.update(foundUser.getId(), foundUser, User.class);
        }
        catch (Exception ex){
            throw new BadRequestException();
        }

        return userToReturn;
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
