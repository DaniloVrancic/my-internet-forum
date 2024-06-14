package org.etf.unibl.SecureForum.service.impl;

import jakarta.transaction.Transactional;
import org.etf.unibl.SecureForum.additional.email.EmailSender;
import org.etf.unibl.SecureForum.base.CrudJpaService;
import org.etf.unibl.SecureForum.exceptions.ConflictException;
import org.etf.unibl.SecureForum.exceptions.ForbiddenException;
import org.etf.unibl.SecureForum.exceptions.NotFoundException;
import org.etf.unibl.SecureForum.model.dto.AuthResponse;
import org.etf.unibl.SecureForum.model.dto.User;
import org.etf.unibl.SecureForum.model.dto.UserWithAuthenticationTokenResponse;
import org.etf.unibl.SecureForum.model.entities.CodeVerificationEntity;
import org.etf.unibl.SecureForum.model.entities.UserEntity;
import org.etf.unibl.SecureForum.model.enums.UserType;
import org.etf.unibl.SecureForum.model.requests.*;
import org.etf.unibl.SecureForum.repositories.CodeVerificationRepository;
import org.etf.unibl.SecureForum.repositories.UserRepository;
import org.etf.unibl.SecureForum.security.JWTGenerator;
import org.etf.unibl.SecureForum.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Random;


@Service
public class UserServiceImpl extends CrudJpaService<UserEntity, Integer> implements UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CodeVerificationRepository codeVerificationRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JWTGenerator jwtGenerator;

    @Autowired
    public UserServiceImpl(AuthenticationManager authenticationManager,
                           ModelMapper modelMapper, UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JWTGenerator jwtGenerator,
                           CodeVerificationRepository codeVerificationRepository,
                           EmailSender emailSender) {
        super(userRepository, modelMapper, UserEntity.class); //For implementing CRUD operations
        this.authenticationManager = authenticationManager;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.codeVerificationRepository = codeVerificationRepository;
        this.emailSender = emailSender;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
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
        userForDatabase.setOauth_account(false);
        ///////////////////////////////////////////////////////
        UserEntity savedUser = null;
        try {
            savedUser = userRepository.save(userForDatabase);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException();
        }

        if (savedUser.getStatus().equals(UserEntity.Status.REQUESTED)) {
            generateNewVerificationCode(savedUser); //Code to send to email for verification
        }
        ////////    SETTING DATA FOR USER TO RETURN //////////
        userToReturn = mapUserEntityToUser(savedUser);
        /////////////////////////////////////////////////////


        return userToReturn;
    }

    public User login(LoginRequest request) {

        try {
            UserEntity foundEntity = userRepository.findByUsernameIs(request.getUsername()).orElseThrow(NotFoundException::new);

            if(!UserEntity.Status.BLOCKED.equals(foundEntity.getStatus())){
                generateNewVerificationCode(foundEntity);
            }

            if(UserEntity.Status.ACTIVE.equals(foundEntity.getStatus())){
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            User userToReturn = mapUserEntityToUser(foundEntity);

            if (userToReturn.getStatus().equals(UserEntity.Status.BLOCKED)) {
                throw new ForbiddenException(); // If the user was blocked, don't allow login
            }

            String token = jwtGenerator.generateToken(foundEntity);


           // return new UserWithAuthenticationTokenResponse(userToReturn, token);
            return userToReturn;
        } catch (BadCredentialsException ex) {
            throw new NotFoundException("User credentials aren't correct"); // If user was not found or password is incorrect
        }

    }

    /**
     * Generates new Verification Code for user to Use, also sends it to the users email.
     *
     * @param savedUser User to send the code to
     */
    public void generateNewVerificationCode(UserEntity savedUser) {

        List<CodeVerificationEntity> existingEntities = codeVerificationRepository.findAllByReferencedUser_Id(savedUser.getId());

        CodeVerificationEntity codeVerificationEntity = null;
        CodeVerificationEntity savedCodeVerificationEntity = null;
        if (existingEntities.size() > 0) /* IF A CODE ALREADY EXISTS FOR A USER IN DATABASE, OVERWRITE IT WITH NEW CODE*/ {
            codeVerificationEntity = existingEntities.get(0);
            String randomGeneratedCode = generateRandomVerificationCode();
            codeVerificationEntity.setVerificationCode(randomGeneratedCode);
            codeVerificationEntity.setCreatedAt(Timestamp.from(Instant.now()));
            savedCodeVerificationEntity = codeVerificationRepository.save(codeVerificationEntity);
        } else { /*OTHERWISE IF IT DOESN'T EXIST, JUST CREATE A NEW ONE*/
            String randomGeneratedCode = generateRandomVerificationCode();

            codeVerificationEntity = new CodeVerificationEntity();
            codeVerificationEntity.setVerificationCode(randomGeneratedCode);
            codeVerificationEntity.setReferencedUser(savedUser);

            codeVerificationEntity.setCreatedAt(Timestamp.from(Instant.now()));

            savedCodeVerificationEntity = codeVerificationRepository.save(codeVerificationEntity);
        }


        try { /*AFTER CREATING OR UPDATING IT, JUST SEND A NEW EMAIL*/
            emailSender.send(savedUser.getEmail(),
                    buildEmail(savedUser.getUsername(), savedCodeVerificationEntity.getVerificationCode()));
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
            throw ex;
        }
    }

    @Override
    public User changePrivileges(UpdateUserPrivilegesRequest request) {
        UserEntity userToUpdate = userRepository.findById(request.getId()).orElseThrow(NotFoundException::new);
        userToUpdate.setType(request.getType());
        userToUpdate.setStatus(request.getStatus());

        UserEntity updatedEntity = userRepository.save(userToUpdate);

        return mapUserEntityToUser(updatedEntity);
    }

    @Override
    public List<CodeVerificationEntity> getAllCodesForUser(Integer userId) {
        return codeVerificationRepository.findAllByReferencedUser_Id(userId);
    }


    private PasswordEncoder getBCryptEncoder() {
        return new BCryptPasswordEncoder();
    }

    private String generateRandomVerificationCode() {
        Random rand = new Random();
        final int NUM_OF_CODE_DIGITS = 6; //DIGITS EXISTING IN CODE

        String codeToReturn = "";
        for (int i = 0; i < NUM_OF_CODE_DIGITS; ++i) {
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
                "<p></p>" +
                "<p><b>" + code + "</b></p>" +
                "<p>We are eagerly waiting for you!</p>" +
                "</body>";

        // Construct a wrapper object to hold both text and HTML content
        return textContent + "|||" + htmlContent;
    }

    private User mapUserEntityToUser(UserEntity entity) {
        User userToReturn = new User();
        userToReturn.setId(entity.getId());
        userToReturn.setUsername(entity.getUsername());
        userToReturn.setEmail(entity.getEmail());
        userToReturn.setCreateTime(entity.getCreateTime());
        userToReturn.setType(entity.getType());
        userToReturn.setStatus(entity.getStatus());
        return userToReturn;
    }

    @Override
    public Boolean logoutUser(LogoutRequest request) {
        UserEntity foundEntity = userRepository.findById(request.userId()).orElseThrow(NotFoundException::new);

        if (!UserEntity.Status.BLOCKED.equals(foundEntity.getStatus())) {
            foundEntity.setStatus(UserEntity.Status.REQUESTED);
            userRepository.save(foundEntity);
            //SecurityContextHolder.clearContext();
            return true;
        } else {
            //SecurityContextHolder.clearContext();
            return false;
        }
    }

}
