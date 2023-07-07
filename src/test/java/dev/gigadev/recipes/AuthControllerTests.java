package dev.gigadev.recipes;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gigadev.recipes.controller.AuthController;
import dev.gigadev.recipes.model.ERole;
import dev.gigadev.recipes.model.Role;
import dev.gigadev.recipes.model.User;
import dev.gigadev.recipes.payload.MessageResponse;
import dev.gigadev.recipes.payload.SignupRequest;
import dev.gigadev.recipes.repository.RoleRepository;
import dev.gigadev.recipes.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


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

    //    @Test
//    void testRegisterUserWithValidPassword() {
//        SignupRequest request = new SignupRequest();
//        request.setUsername("testuser");
//        request.setEmail("test@example.com");
//        request.setPassword("validpassword");
//        request.setRoles(Collections.singleton("user"));
//
//        Role userRole = new Role();
//        userRole.setName(ERole.ROLE_USER);
//        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));
//
//        User savedUser = new User("testuser", "test@example.com", "encodedpassword");
//        when(userRepository.existsByUsername("testuser")).thenReturn(false);
//        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
//        when(passwordEncoder.encode("validpassword")).thenReturn("encodedpassword");
//        when(userRepository.save(any(User.class))).thenReturn(savedUser);
//
//        ResponseEntity<?> responseEntity = authController.registerUser(request);
//
//        assertEquals(200, responseEntity.getStatusCodeValue());
//        assertTrue(responseEntity.getBody() instanceof MessageResponse);
//        MessageResponse response = (MessageResponse) responseEntity.getBody();
//        assertEquals("User registered successfully!", response.getMessage());
//    }
//    @Test
//    void testRegisterUserWithNullPassword() {
//        SignupRequest request2 = new SignupRequest();
//        request2.setPassword(null);
//        request2.setUsername("testuser");
//        request2.setEmail("test@example.com");
//        request2.setRoles(Collections.singleton("user"));
//
//        Role userRole = new Role();
//        userRole.setName(ERole.ROLE_USER);
//        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));
//
//        ResponseEntity<?> responseEntity = authController.registerUser(request2);
//
//
//        System.out.println(responseEntity.getBody());
//
//        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//    }
    @Test
    void testRegisterUserWithBlankPassword() throws Exception{
        SignupRequest request2 = new SignupRequest("testuser", "test@example.com", Collections.singleton("user"), "");
        mockMvc.perform(post("/api/auth/register", 42L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isBadRequest());
    }

//    @Test
//    void testRegisterUserWithBlankUser() {
//        SignupRequest request = new SignupRequest();
//        request.setUsername(""); // Set username to blank
//        request.setEmail("test@example.com");
//        request.setPassword("validpassword");
//        request.setRoles(Collections.singleton("user"));
//
//        Role userRole = new Role();
//        userRole.setName(ERole.ROLE_USER);
//        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));
//
//        ResponseEntity<?> responseEntity = authController.registerUser(request);
//
//        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//
//    }
//    @Test
//    void testRegisterUserWithInvalidLogin() {
//        SignupRequest request = new SignupRequest();
//        request.setUsername("123"); // Set username to blank
//        request.setEmail("test@example.com");
//        request.setPassword("validpassword");
//        request.setRoles(Collections.singleton("user"));
//
//        Role userRole = new Role();
//        userRole.setName(ERole.ROLE_USER);
//        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));
//
//        ResponseEntity<?> responseEntity = authController.registerUser(request);
//
//        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//
//    }
//    @Test
//    void testRegisterUserWithInvalidPassword() {
//        SignupRequest request = new SignupRequest();
//        request.setUsername("aboba"); // Set username to blank
//        request.setEmail("test@example.com");
//        request.setPassword("invalid");
//        request.setRoles(Collections.singleton("user"));
//
//        Role userRole = new Role();
//        userRole.setName(ERole.ROLE_USER);
//        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));
//
//        ResponseEntity<?> responseEntity = authController.registerUser(request);
//
//        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//
//    }
//    @Test
//    void testRegisterUserWithNullUser() {
//        SignupRequest request = new SignupRequest();
//        request.setUsername(null); // Set username to blank
//        request.setEmail("test@example.com");
//        request.setPassword("validpassword");
//        request.setRoles(Collections.singleton("user"));
//
//        Role userRole = new Role();
//        userRole.setName(ERole.ROLE_USER);
//        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));
//
//        ResponseEntity<?> responseEntity = authController.registerUser(request);
//
//        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//
//    }
//



}
