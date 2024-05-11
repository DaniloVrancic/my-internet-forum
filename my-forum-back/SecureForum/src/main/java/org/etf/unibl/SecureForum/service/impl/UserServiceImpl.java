package org.etf.unibl.SecureForum.service.impl;

import jakarta.transaction.Transactional;
import org.etf.unibl.SecureForum.additional.email.EmailSender;
import org.etf.unibl.SecureForum.base.CrudJpaService;
import org.etf.unibl.SecureForum.model.dto.User;
import org.etf.unibl.SecureForum.model.entities.CodeVerificationEntity;
import org.etf.unibl.SecureForum.model.entities.UserEntity;
import org.etf.unibl.SecureForum.model.requests.ChangeRoleRequest;
import org.etf.unibl.SecureForum.model.requests.ChangeStatusRequest;
import org.etf.unibl.SecureForum.model.requests.SignUpRequest;
import org.etf.unibl.SecureForum.model.requests.UserUpdateRequest;
import org.etf.unibl.SecureForum.repositories.CodeVerificationRepository;
import org.etf.unibl.SecureForum.repositories.UserRepository;
import org.etf.unibl.SecureForum.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        userForDatabase.setType(request.getType());

        if(request.getStatus() != null) //if the request specifies the status, assign that status...
        {
            userForDatabase.setStatus(request.getStatus());
        }
        else{ //or else, by default, assign the Status to REQUESTED upon creating the entity
            userForDatabase.setStatus(UserEntity.Status.REQUESTED);
        }

        UserEntity savedUser = userRepository.save(userForDatabase);

        if(savedUser.getStatus().equals(UserEntity.Status.REQUESTED))
        {
            String randomGeneratedCode = generateRandomVerificationCode();

            CodeVerificationEntity codeVerificationEntity = new CodeVerificationEntity();
            codeVerificationEntity.setVerificationCode(randomGeneratedCode);
            codeVerificationEntity.setReferencedUser(savedUser);

            CodeVerificationEntity savedCodeVerificationEntity =  codeVerificationRepository.save(codeVerificationEntity);

            try{
                emailSender.send(savedUser.getEmail(),
                                buildEmail(savedUser.getUsername(), savedCodeVerificationEntity.getVerificationCode()));
            }
            catch(Exception ex){
                System.out.println(ex.getLocalizedMessage());
                throw ex;
            }
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

    @Override
    public User changeStatus(Integer userId, ChangeStatusRequest request) {
        //TODO: IMPLEMENT METHOD
        return null;
    }

    @Override
    public User changeRole(Integer userId, ChangeRoleRequest request) {
        //TODO: IMPLEMENT METHOD
        return null;
    }

    @Override
    public User update(Integer id, UserUpdateRequest request) {
        //TODO: IMPLEMENT METHOD
        return null;
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
                "<p><a href=\"" + code + "\">Activate Now</a></p>" +
                "<p>We are eagerly waiting for you!</p>" +
                "</body>";

        // Construct a wrapper object to hold both text and HTML content
        return textContent + "|||" + htmlContent;
    }

}
