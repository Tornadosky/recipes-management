package dev.gigadev.recipes;

import dev.gigadev.recipes.controller.AuthController;
import dev.gigadev.recipes.model.ERole;
import dev.gigadev.recipes.model.Role;
import dev.gigadev.recipes.model.User;
import dev.gigadev.recipes.payload.LoginRequest;
import dev.gigadev.recipes.payload.MessageResponse;
import dev.gigadev.recipes.payload.SignupRequest;
import dev.gigadev.recipes.repository.RoleRepository;
import dev.gigadev.recipes.repository.UserRepository;
import io.jsonwebtoken.lang.Assert;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.AssertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.ErrorResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class AuthControllerTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUserWithValidPassword() {
        SignupRequest request = new SignupRequest();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("validpassword");
        request.setRoles(Collections.singleton("user"));

        Role userRole = new Role();
        userRole.setName(ERole.ROLE_USER);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));

        User savedUser = new User("testuser", "test@example.com", "encodedpassword");
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("validpassword")).thenReturn("encodedpassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        ResponseEntity<?> responseEntity = authController.registerUser(request);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertTrue(responseEntity.getBody() instanceof MessageResponse);
        MessageResponse response = (MessageResponse) responseEntity.getBody();
        assertEquals("User registered successfully!", response.getMessage());
    }
    @Test
    void testRegisterUserWithNullPassword() {
        SignupRequest request2 = new SignupRequest();
        request2.setPassword(null);
        request2.setUsername("testuser");
        request2.setEmail("test@example.com");
        request2.setRoles(Collections.singleton("user"));

        Role userRole = new Role();
        userRole.setName(ERole.ROLE_USER);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));

        ResponseEntity<?> responseEntity = authController.registerUser(request2);


        System.out.println(responseEntity.getBody());

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    @Test
    void testRegisterUserWithBlankPassword() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        SignupRequest request2 = new SignupRequest();
        request2.setPassword("");
        request2.setUsername("testuser");
        request2.setEmail("test@example.com");
        //request2.setRoles(Collections.singleton("user"));
        // Validate the request object (password is not blank and has some length)
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request2);
        System.out.println(violations);
        System.out.println(violations.isEmpty());
        assertFalse(violations.isEmpty());
    }
    @Test
    void testRegisterUserWithBlankUser() {
        SignupRequest request = new SignupRequest();
        request.setUsername(""); // Set username to blank
        request.setEmail("test@example.com");
        request.setPassword("validpassword");
        request.setRoles(Collections.singleton("user"));

        Role userRole = new Role();
        userRole.setName(ERole.ROLE_USER);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));

        ResponseEntity<?> responseEntity = authController.registerUser(request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }
    @Test
    void testRegisterUserWithInvalidLogin() {
        SignupRequest request = new SignupRequest();
        request.setUsername("123"); // Set username to blank
        request.setEmail("test@example.com");
        request.setPassword("validpassword");
        request.setRoles(Collections.singleton("user"));

        Role userRole = new Role();
        userRole.setName(ERole.ROLE_USER);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));

        ResponseEntity<?> responseEntity = authController.registerUser(request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }
    @Test
    void testRegisterUserWithInvalidPassword() {
        SignupRequest request = new SignupRequest();
        request.setUsername("aboba"); // Set username to blank
        request.setEmail("test@example.com");
        request.setPassword("invalid");
        request.setRoles(Collections.singleton("user"));

        Role userRole = new Role();
        userRole.setName(ERole.ROLE_USER);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));

        ResponseEntity<?> responseEntity = authController.registerUser(request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }
    @Test
    void testRegisterUserWithNullUser() {
        SignupRequest request = new SignupRequest();
        request.setUsername(null); // Set username to blank
        request.setEmail("test@example.com");
        request.setPassword("validpassword");
        request.setRoles(Collections.singleton("user"));

        Role userRole = new Role();
        userRole.setName(ERole.ROLE_USER);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));

        ResponseEntity<?> responseEntity = authController.registerUser(request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }




}
