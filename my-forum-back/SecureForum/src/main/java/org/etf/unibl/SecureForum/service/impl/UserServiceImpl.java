package org.etf.unibl.SecureForum.service.impl;

import jakarta.transaction.Transactional;
import org.etf.unibl.SecureForum.additional.email.EmailSender;
import org.etf.unibl.SecureForum.base.CrudJpaService;
import org.etf.unibl.SecureForum.exceptions.NotFoundException;
import org.etf.unibl.SecureForum.model.dto.User;
import org.etf.unibl.SecureForum.model.entities.CodeVerificationEntity;
import org.etf.unibl.SecureForum.model.entities.UserEntity;
import org.etf.unibl.SecureForum.model.enums.UserType;
import org.etf.unibl.SecureForum.model.requests.*;
import org.etf.unibl.SecureForum.repositories.CodeVerificationRepository;
import org.etf.unibl.SecureForum.repositories.UserRepository;
import org.etf.unibl.SecureForum.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Random;


@Service
@Transactional
public class UserServiceImpl extends CrudJpaService<UserEntity, Integer> implements UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CodeVerificationRepository codeVerificationRepository;
    private final EmailSender emailSender;

    @Autowired
    public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository,
                           CodeVerificationRepository codeVerificationRepository, EmailSender emailSender)
    {
        super(userRepository, modelMapper, UserEntity.class); //For implementing CRUD operations
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.codeVerificationRepository = codeVerificationRepository;
        this.emailSender = emailSender;
    }
    
    


    @Override
    public User signUp(SignUpRequest request) {
        UserEntity userForDatabase = new UserEntity(); //Entity to place in database
        User userToReturn = new User(); //RETURNING DTO Object


        PasswordEncoder encoder = getBCryptEncoder();   //Getting encoder for hashing

        userForDatabase.setPassword(encoder.encode(request.getPassword())); //hashing the password for database
        userForDatabase.setUsername(request.getUsername());
        userForDatabase.setEmail(request.getEmail());

        //      DEFAULT VALUES FOR USERS THAT SIGN UP   ///////
        userForDatabase.setStatus(UserEntity.Status.REQUESTED);
        userForDatabase.setCreateTime(Timestamp.from(Instant.now()));
        userForDatabase.setType(UserType.Forumer);
        ///////////////////////////////////////////////////////

        UserEntity savedUser = userRepository.save(userForDatabase);

        if(savedUser.getStatus().equals(UserEntity.Status.REQUESTED))
        {
            generateNewVerificationCode(savedUser);
        }
        ////////    SETTING DATA FOR USER TO RETURN //////////
        userToReturn.setUsername(savedUser.getUsername());
        userToReturn.setEmail(savedUser.getEmail());
        userToReturn.setCreateTime(savedUser.getCreateTime());
        userToReturn.setType(savedUser.getType());
        userToReturn.setStatus(savedUser.getStatus());
        /////////////////////////////////////////////////////
        return userToReturn;
    }

    public User login(LoginRequest request){
        UserEntity foundEntity = userRepository.findByUsernameIs(request.getUsername()).orElseThrow(NotFoundException::new);
        User userToReturn = null;

        if(foundEntity != null){
            userToReturn = new User();
            userToReturn.setId(foundEntity.getId());
            userToReturn.setUsername(foundEntity.getUsername());
            userToReturn.setEmail(foundEntity.getEmail());
            userToReturn.setType(foundEntity.getType());
            userToReturn.setStatus(foundEntity.getStatus());
            userToReturn.setCreateTime(foundEntity.getCreateTime());
        }
        else{
            throw new NotFoundException();
        }

        return userToReturn;
    }

    /**
     * Generates new Verification Code for user to Use, also sends it to the users email.
     * @param savedUser User to send the code to
     */
    public void generateNewVerificationCode(UserEntity savedUser) {

        List<CodeVerificationEntity> existingEntities = codeVerificationRepository.findAllByReferencedUser_Id(savedUser.getId());

        CodeVerificationEntity codeVerificationEntity = null;
        CodeVerificationEntity savedCodeVerificationEntity = null;
        if(existingEntities.size() > 0) /* IF A CODE ALREADY EXISTS FOR A USER IN DATABASE, OVERWRITE IT WITH NEW CODE*/
        {
            codeVerificationEntity = existingEntities.get(0);
            String randomGeneratedCode = generateRandomVerificationCode();
            codeVerificationEntity.setVerificationCode(randomGeneratedCode);
            codeVerificationEntity.setCreatedAt(Timestamp.from(Instant.now()));
            savedCodeVerificationEntity = codeVerificationRepository.save(codeVerificationEntity);
        }
        else{ /*OTHERWISE IF IT DOESN'T EXIST, JUST CREATE A NEW ONE*/
            String randomGeneratedCode = generateRandomVerificationCode();

            codeVerificationEntity = new CodeVerificationEntity();
            codeVerificationEntity.setVerificationCode(randomGeneratedCode);
            codeVerificationEntity.setReferencedUser(savedUser);

            codeVerificationEntity.setCreatedAt(Timestamp.from(Instant.now()));

            savedCodeVerificationEntity =  codeVerificationRepository.save(codeVerificationEntity);
        }




        try{ /*AFTER CREATING OR UPDATING IT, JUST SEND A NEW EMAIL*/
            emailSender.send(savedUser.getEmail(),
                            buildEmail(savedUser.getUsername(), savedCodeVerificationEntity.getVerificationCode()));
        }
        catch(Exception ex){
            System.out.println(ex.getLocalizedMessage());
            throw ex;
        }
    }

    @Override
    public User changeStatus(ChangeStatusRequest request) {
        UserEntity userToUpdate = userRepository.findById(request.getId()).orElseThrow(NotFoundException::new);
        userToUpdate.setStatus(request.getStatus());

        User userToReturn = new User();
        userToReturn.setUsername(userToUpdate.getUsername());
        userToReturn.setEmail(userToUpdate.getEmail());
        userToReturn.setCreateTime(userToUpdate.getCreateTime());
        userToReturn.setType(userToUpdate.getType());
        userToReturn.setStatus(userToUpdate.getStatus());

        if(request.getStatus().equals(userToUpdate.getStatus())) //If the status is the same, don't change it
        {
            return userToReturn; //but still return all the data as if it changed
        }

        UserEntity updatedEntity = userRepository.save(userToUpdate);

        return userToReturn;
    }

    @Override
    public User changeRole(ChangeTypeRequest request) {
        UserEntity userToUpdate = userRepository.findById(request.getId()).orElseThrow(NotFoundException::new);
        userToUpdate.setType(request.getType());

        User userToReturn = new User();
        userToReturn.setUsername(userToUpdate.getUsername());
        userToReturn.setEmail(userToUpdate.getEmail());
        userToReturn.setCreateTime(userToUpdate.getCreateTime());
        userToReturn.setType(userToUpdate.getType());
        userToReturn.setStatus(userToUpdate.getStatus());

        if(request.getType().equals(userToUpdate.getType())) //If the type is the same, don't change it
        {
            return userToReturn; //but still return all the data as if it changed
        }

        UserEntity updatedEntity = userRepository.save(userToUpdate);

        return userToReturn;
    }

    @Override
    public List<CodeVerificationEntity> getAllCodesForUser(Integer userId)
    {
        return codeVerificationRepository.findAllByReferencedUser_Id(userId);
    }


    private PasswordEncoder getBCryptEncoder(){
        return new BCryptPasswordEncoder();
    }

    private String generateRandomVerificationCode()
    {
        Random rand = new Random();
        final int NUM_OF_CODE_DIGITS = 6; //DIGITS EXISTING IN CODE

        String codeToReturn = "";
        for(int i = 0; i < NUM_OF_CODE_DIGITS; ++i) {
            codeToReturn += rand.nextInt(10);
        }

        return codeToReturn;
    }

    public String buildEmail(String name, String code) {
        String textContent = "Hi " + name + ",\n\n" +
                "Thank you for registering to MySecureForum. Please use the code below to activate your account:\n" +
                code + "\n\n" +
                "We are eagerly waiting for you!";

        String htmlContent = "<body>" +
                "<p>Hi " + name + ",</p>" +
                "<p>Thank you for registering to MySecureForum. Please use the code below to activate your account:</p>" +
                "<p></p>"+
                "<p><b>" + code + "</b></p>" +
                "<p>We are eagerly waiting for you!</p>" +
                "</body>";

        // Construct a wrapper object to hold both text and HTML content
        return textContent + "|||" + htmlContent;
    }

}
