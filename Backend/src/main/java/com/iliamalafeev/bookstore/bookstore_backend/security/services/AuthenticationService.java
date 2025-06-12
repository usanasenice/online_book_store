package com.iliamalafeev.bookstore.bookstore_backend.security.services;

import com.iliamalafeev.bookstore.bookstore_backend.entities.Person;
import com.iliamalafeev.bookstore.bookstore_backend.repositories.PersonRepository;
import com.iliamalafeev.bookstore.bookstore_backend.security.dto.requests.PersonLoginDTO;
import com.iliamalafeev.bookstore.bookstore_backend.security.dto.requests.PersonRegistrationDTO;
import com.iliamalafeev.bookstore.bookstore_backend.security.dto.responses.AuthenticationResponse;
import com.iliamalafeev.bookstore.bookstore_backend.security.entities.PersonDetails;
import com.iliamalafeev.bookstore.bookstore_backend.security.entities.Role;
import com.iliamalafeev.bookstore.bookstore_backend.utils.exceptions.PersonNotCreatedException;
import com.iliamalafeev.bookstore.bookstore_backend.utils.exceptions.PersonNotFoundException;
import com.iliamalafeev.bookstore.bookstore_backend.utils.validators.PersonValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final PersonValidator personValidator;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(PersonValidator personValidator, PersonRepository personRepository, 
                               PasswordEncoder passwordEncoder, JwtService jwtService, 
                               AuthenticationManager authenticationManager) {
        this.personValidator = personValidator;
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse registerPerson(PersonRegistrationDTO personRegistrationDTO, BindingResult bindingResult) {
        logger.debug("Starting registration process for email: {}", personRegistrationDTO.getEmail());
        
        try {
            // Check if user already exists
            Optional<Person> existingPerson = personRepository.findByEmail(personRegistrationDTO.getEmail());
            if (existingPerson.isPresent()) {
                String errorMsg = "User with email " + personRegistrationDTO.getEmail() + " already exists";
                logger.error(errorMsg);
                throw new PersonNotCreatedException(errorMsg);
            }

            // Create new person
            Person person = new Person();
            person.setFirstName(personRegistrationDTO.getFirstName());
            person.setLastName(personRegistrationDTO.getLastName());
            person.setDateOfBirth(personRegistrationDTO.getDateOfBirth());
            person.setEmail(personRegistrationDTO.getEmail());
            person.setPassword(passwordEncoder.encode(personRegistrationDTO.getPassword()));
            person.setRole(Role.ROLE_USER);
            person.setRegisteredAt(Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("UTC")).toLocalDateTime());

            // Validate person
            logger.debug("Validating person data");
            personValidator.validate(person, bindingResult);
            if (bindingResult.hasErrors()) {
                String errorMsg = "Validation failed: " + bindingResult.getAllErrors().toString();
                logger.error(errorMsg);
                throw new PersonNotCreatedException(errorMsg);
            }

            // Save person
            logger.debug("Saving person to database");
            personRepository.save(person);
            logger.info("Successfully registered new user with email: {}", person.getEmail());

            // Generate token
            String jwtToken = jwtService.generateToken(new PersonDetails(person));
            return new AuthenticationResponse(jwtToken);

        } catch (Exception e) {
            logger.error("Registration failed: {}", e.getMessage(), e);
            throw new PersonNotCreatedException("Registration failed: " + e.getMessage());
        }
    }

    public AuthenticationResponse authenticatePerson(PersonLoginDTO personLoginDTO, BindingResult bindingResult) {
        logger.debug("Starting authentication process for email: {}", personLoginDTO.getEmail());
        
        try {
            // Validate input
            if (bindingResult.hasErrors()) {
                String errorMsg = "Validation failed: " + bindingResult.getAllErrors().toString();
                logger.error(errorMsg);
                throw new PersonNotFoundException(errorMsg);
            }

            // Check if user exists
            Optional<Person> person = personRepository.findByEmail(personLoginDTO.getEmail());
            if (person.isEmpty()) {
                String errorMsg = "User with email " + personLoginDTO.getEmail() + " not found";
                logger.error(errorMsg);
                throw new PersonNotFoundException(errorMsg);
            }

            // Authenticate
            try {
                logger.debug("Attempting to authenticate user");
                authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        personLoginDTO.getEmail(),
                        personLoginDTO.getPassword()
                    )
                );
            } catch (BadCredentialsException e) {
                String errorMsg = "Invalid credentials for user: " + personLoginDTO.getEmail();
                logger.error(errorMsg);
                throw new PersonNotFoundException(errorMsg);
            }

            // Generate token
            String jwtToken = jwtService.generateToken(new PersonDetails(person.get()));
            logger.info("Successfully authenticated user with email: {}", personLoginDTO.getEmail());
            return new AuthenticationResponse(jwtToken);

        } catch (PersonNotFoundException e) {
            logger.error("Authentication failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during authentication: {}", e.getMessage(), e);
            throw new PersonNotFoundException("Authentication failed: " + e.getMessage());
        }
    }
}
