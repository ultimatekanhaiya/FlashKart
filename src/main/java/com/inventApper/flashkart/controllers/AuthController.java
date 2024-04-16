package com.inventApper.flashkart.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.inventApper.flashkart.dtos.JwtRequest;
import com.inventApper.flashkart.dtos.JwtResponse;
import com.inventApper.flashkart.dtos.UserDto;
import com.inventApper.flashkart.entities.User;
import com.inventApper.flashkart.exceptions.BadApiRequestException;
import com.inventApper.flashkart.security.JwtHelper;
import com.inventApper.flashkart.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper helper;

    @Autowired
    private ModelMapper mapper;

    @Value("${googleClientId}")
    private String googleClientId;

    @Value("${newPassword}")
    private String newPassword;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        this.doAuthentication(request.getEmail(), request.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);
        UserDto userDto = mapper.map(userDetails, UserDto.class);
        return ResponseEntity.ok(new JwtResponse(token, userDto));
    }

    private void doAuthentication(String email, String password) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,
                password);
        try {
            manager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            throw new BadApiRequestException("Invalid username or password !!");
        }
    }

    // login with google api

    @PostMapping("/google")
    public ResponseEntity<JwtResponse> loginWithGoogle(@RequestBody Map<String, Object> data) throws IOException {

        // get the id token from request
        String idToken = data.get("idToken").toString();

        NetHttpTransport netHttpTransport = new NetHttpTransport();

        JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, jacksonFactory)
                .setAudience(Collections.singleton(googleClientId));

        GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), idToken);

        GoogleIdToken.Payload payload = googleIdToken.getPayload();

        logger.info("Payload : {}", payload);

        String email = payload.getEmail();

        User user = null;

        user = userService.findUserByEmailOptional(email).orElse(null);
        if (user == null) {
            // create new user
            user = this.saveUser(email, data.get("name").toString(), data.get("photoUrl").toString());
        }
        ResponseEntity<JwtResponse> jwtResponseResponseEntity = this
                .login(JwtRequest.builder().email(user.getEmail()).password(newPassword).build());
        return jwtResponseResponseEntity;

    }

    private User saveUser(String email, String name, String photoUrl) {
        UserDto newUser = UserDto.builder()
                .name(name)
                .email(email)
                .password(newPassword)
                .imageName(photoUrl)
                .roles(new HashSet<>())
                .build();

        UserDto user = userService.createUser(newUser);

        return this.mapper.map(user, User.class);

    }
}
