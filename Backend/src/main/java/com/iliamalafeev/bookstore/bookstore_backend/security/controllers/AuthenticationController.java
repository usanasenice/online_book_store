package com.iliamalafeev.bookstore.bookstore_backend.security.controllers;

import com.iliamalafeev.bookstore.bookstore_backend.security.dto.requests.PersonLoginDTO;
import com.iliamalafeev.bookstore.bookstore_backend.security.dto.requests.PersonRegistrationDTO;
import com.iliamalafeev.bookstore.bookstore_backend.security.dto.responses.AuthenticationResponse;
import com.iliamalafeev.bookstore.bookstore_backend.security.services.AuthenticationService;
import com.iliamalafeev.bookstore.bookstore_backend.utils.error_responses.PersonErrorResponse;
import com.iliamalafeev.bookstore.bookstore_backend.utils.exceptions.PersonNotCreatedException;
import com.iliamalafeev.bookstore.bookstore_backend.utils.exceptions.PersonNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication API")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account and returns a JWT token")
    public ResponseEntity<?> register(@RequestBody @Valid PersonRegistrationDTO personRegistrationDTO,
                                    BindingResult bindingResult) {
        try {
            logger.info("Attempting to register new user with email: {}", personRegistrationDTO.getEmail());
            
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
                logger.error("Validation errors during registration: {}", errorMessage);
                return ResponseEntity.badRequest().body(new PersonErrorResponse(errorMessage, System.currentTimeMillis()));
            }

            AuthenticationResponse response = authenticationService.registerPerson(personRegistrationDTO, bindingResult);
            logger.info("Successfully registered user with email: {}", personRegistrationDTO.getEmail());
            return ResponseEntity.ok(response);
            
        } catch (PersonNotCreatedException e) {
            logger.error("Failed to create user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new PersonErrorResponse(e.getMessage(), System.currentTimeMillis()));
        } catch (Exception e) {
            logger.error("Unexpected error during registration: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new PersonErrorResponse("An unexpected error occurred: " + e.getMessage(), System.currentTimeMillis()));
        }
    }

    @PostMapping("/authenticate")
    @Operation(summary = "Authenticate a user", description = "Authenticates a user and returns a JWT token")
    public ResponseEntity<?> authenticate(@RequestBody @Valid PersonLoginDTO personLoginDTO,
                                        BindingResult bindingResult) {
        try {
            logger.info("Attempting to authenticate user with email: {}", personLoginDTO.getEmail());
            
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
                logger.error("Validation errors during authentication: {}", errorMessage);
                return ResponseEntity.badRequest().body(new PersonErrorResponse(errorMessage, System.currentTimeMillis()));
            }

            AuthenticationResponse response = authenticationService.authenticatePerson(personLoginDTO, bindingResult);
            logger.info("Successfully authenticated user with email: {}", personLoginDTO.getEmail());
            return ResponseEntity.ok(response);
            
        } catch (PersonNotFoundException e) {
            logger.error("User not found during authentication: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new PersonErrorResponse(e.getMessage(), System.currentTimeMillis()));
        } catch (Exception e) {
            logger.error("Unexpected error during authentication: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new PersonErrorResponse("An unexpected error occurred: " + e.getMessage(), System.currentTimeMillis()));
        }
    }
}