package dev.gigadev.recipes.controller;

import dev.gigadev.recipes.model.ERole;
import dev.gigadev.recipes.model.Role;
import dev.gigadev.recipes.model.User;
import dev.gigadev.recipes.payload.LoginRequest;
import dev.gigadev.recipes.payload.MessageResponse;
import dev.gigadev.recipes.payload.SignupRequest;
import dev.gigadev.recipes.payload.UserInfoResponse;
import dev.gigadev.recipes.repository.RoleRepository;
import dev.gigadev.recipes.repository.UserRepository;
import dev.gigadev.recipes.security.JwtUtils;
import dev.gigadev.recipes.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.BooleanOperators.Or.or;

@CrossOrigin(origins = "*", maxAge = 3600)
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
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String jwtCookieString = jwtCookie != null ? jwtCookie.toString() : null;

        if (jwtCookieString != null) {
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookieString)
                    .body(new UserInfoResponse(userDetails.getId(),
                            userDetails.getUsername(),
                            userDetails.getEmail(),
                            roles));
        } else {
            // Handle the situation when jwtCookie is null
            // You can throw an exception, return an error response, or handle it in any way that suits your application's logic
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error: JWT cookie is null."));
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }
        if (signUpRequest.getPassword() == null || signUpRequest.getPassword() == ""){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Password cannot be null!"));
        }
        if (signUpRequest.getUsername() == null || signUpRequest.getUsername() == "") {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username cannot be null!"));
        }
        if (signUpRequest.getPassword().length() <8) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username cannot be shorter than 8 symbols!"));
        }
        if (signUpRequest.getUsername().length() <5) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username cannot be shorter than 8 symbols!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    }
                    case "mod" -> {
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}