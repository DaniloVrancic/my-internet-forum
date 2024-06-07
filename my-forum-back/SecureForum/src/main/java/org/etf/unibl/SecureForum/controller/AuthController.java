package org.etf.unibl.SecureForum.controller;

import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.etf.unibl.SecureForum.exceptions.BadRequestException;
import org.etf.unibl.SecureForum.exceptions.ForbiddenException;
import org.etf.unibl.SecureForum.exceptions.NotFoundException;
import org.etf.unibl.SecureForum.exceptions.UnauthorizedException;
import org.etf.unibl.SecureForum.model.dto.AuthResponse;
import org.etf.unibl.SecureForum.model.dto.User;
import org.etf.unibl.SecureForum.model.dto.UserWithAuthenticationTokenResponse;
import org.etf.unibl.SecureForum.model.dto.VerificationCodeEmailMessage;
import org.etf.unibl.SecureForum.model.entities.CodeVerificationEntity;
import org.etf.unibl.SecureForum.model.entities.UserEntity;
import org.etf.unibl.SecureForum.model.requests.LoginRequest;
import org.etf.unibl.SecureForum.model.requests.SignUpRequest;
import org.etf.unibl.SecureForum.model.requests.VerifyUserRequest;
import org.etf.unibl.SecureForum.repositories.CodeVerificationRepository;
import org.etf.unibl.SecureForum.repositories.UserRepository;
import org.etf.unibl.SecureForum.security.JWTGenerator;
import org.etf.unibl.SecureForum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Has endpoints:
 * /auth/login
 * /auth/register
 * /auth/verify
 * /auth/resend_code/{id}
 */
@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserRepository userRepository;
    private final JWTGenerator jwtGenerator;

    private final CodeVerificationRepository codeVerificationRepository;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService,
                          JWTGenerator jwtGenerator,
                          UserRepository userRepository,
                          CodeVerificationRepository codeVerificationRepository) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtGenerator = jwtGenerator;
        this.userRepository = userRepository;
        this.codeVerificationRepository = codeVerificationRepository;
    }

    @PostMapping("/login")
    public UserWithAuthenticationTokenResponse loginUser(@Valid @RequestBody LoginRequest request){

        try{


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                       request.getUsername(),
                        request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserWithAuthenticationTokenResponse authResponse = userService.login(request);

        return authResponse;
        }
        catch(LockedException ex){ //In case that the user has been blocked from the forum will occur
            throw new ForbiddenException("User has been blocked from forum");
        }

    }

    @PostMapping("/verify")
    @Transactional
    public UserWithAuthenticationTokenResponse verifyUser(@RequestBody VerifyUserRequest request)
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

            String jwtToken = jwtGenerator.generateToken(userToVerify);

            return new UserWithAuthenticationTokenResponse(savedUser, jwtToken);
        }
        else{
            throw new NotFoundException();
        }
    }

    @PostMapping("/resend_code/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public VerificationCodeEmailMessage resendCode(@PathVariable Integer id) {
        UserEntity foundUser = userRepository.findById(id).orElseThrow(NotFoundException::new);
        final String SUCCESS_MESSAGE = "Successfully sent a new verification code to: " + foundUser.getEmail();

        userService.generateNewVerificationCode(foundUser);
        VerificationCodeEmailMessage returnMessage = new VerificationCodeEmailMessage();
        returnMessage.setSendingMessage(SUCCESS_MESSAGE);
        return returnMessage;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@Valid @RequestBody SignUpRequest request){
        User newUser = userService.signUp(request);

        return newUser;
    }
}
