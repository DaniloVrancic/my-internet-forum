package org.etf.unibl.SecureForum.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.etf.unibl.SecureForum.exceptions.ForbiddenException;
import org.etf.unibl.SecureForum.exceptions.NotFoundException;
import org.etf.unibl.SecureForum.model.dto.*;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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

    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-secret}")
    private String clientSecret;

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

    @GetMapping("/oauth2")
    public ResponseEntity<UrlDto> oauth2Auth(){
        String url = new GoogleAuthorizationCodeRequestUrl(
                clientId,
                "https://localhost:4200/callback", //callback url for Google to call
                Arrays.asList("email", "profile", "openid")
        ).build(); //Will generate the link where the user will be able to see the Google login form



        return ResponseEntity.ok(new UrlDto(url));
    }

    @GetMapping("/oauth2/callback")
    public ResponseEntity<TokenDto> oauth2AuthCallback(@RequestParam("code") String code) {
        String token = "";
        try{

        token = new GoogleAuthorizationCodeTokenRequest(
                new NetHttpTransport(),
                new GsonFactory(),
                clientId,
                clientSecret,
                code,
                "https://localhost:4200"
        ).execute().getAccessToken();
        System.out.println("I AM IN GOOGLE CALLBACK!");

        }
        catch(Exception ex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return  ResponseEntity.ok(new TokenDto(token));
    }

    @PostMapping("/login")
    public User loginUser(@Valid @RequestBody LoginRequest request){

        try{

        //UserWithAuthenticationTokenResponse authResponse = userService.login(request);

        return userService.login(request);
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
