package com.boubyan.hackathon.banchar.restcontrollers;

import java.net.URI;
import java.util.Collections;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.boubyan.hackathon.banchar.common.RoleName;
import com.boubyan.hackathon.banchar.entities.Role;
import com.boubyan.hackathon.banchar.entities.User;
import com.boubyan.hackathon.banchar.exceptions.AppException;
import com.boubyan.hackathon.banchar.payloads.ApiResponse;
import com.boubyan.hackathon.banchar.payloads.JwtAuthenticationResponse;
import com.boubyan.hackathon.banchar.payloads.LoginRequest;
import com.boubyan.hackathon.banchar.payloads.SignUpRequest;
import com.boubyan.hackathon.banchar.repositories.RoleRepository;
import com.boubyan.hackathon.banchar.repositories.UserRepository;
import com.boubyan.hackathon.banchar.security.JwtTokenProvider;
import com.boubyan.hackathon.banchar.services.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;
    
    @Autowired
    UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        User thisNewUser = userRepository.findByEmail(loginRequest.getUsernameOrEmail()).get();
        Boolean isDriver = thisNewUser.getType() == 1;
        
        SecurityContextHolder.getContext().setAuthentication(authentication);

        
        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, thisNewUser.getId(), isDriver));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword(), signUpRequest.getType(), signUpRequest.getCarType(), signUpRequest.getLicensePlate());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        
        User thisNewUser = userRepository.findByEmail(signUpRequest.getEmail()).get();
        
        Boolean isDriver = thisNewUser.getType() == 1;
        
        if(signUpRequest.getType() == 1) {
        	userService.addDetailsToDriver(signUpRequest.getLicensePlate(), thisNewUser.getId());
        }
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                		signUpRequest.getEmail(),
                		signUpRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt,thisNewUser.getId(), isDriver));
        
    }
}
