package com.daniilSergachev.CloudFileStorage.services;

import com.daniilSergachev.CloudFileStorage.Exception.AppError;
import com.daniilSergachev.CloudFileStorage.dto.JwtRequest;
import com.daniilSergachev.CloudFileStorage.dto.JwtResponse;
import com.daniilSergachev.CloudFileStorage.dto.RegistrationUserDto;
import com.daniilSergachev.CloudFileStorage.dto.UserDto;
import com.daniilSergachev.CloudFileStorage.model.User;
import com.daniilSergachev.CloudFileStorage.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity createAuthToken(JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            logger.warn("Bad credentials for user: {}", authRequest.getUsername());
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Неправильный логин или пароль"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity createNewUser(RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            logger.warn("Passwords do not match for user: {}", registrationUserDto.getUsername());
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пароли не совпадают"), HttpStatus.BAD_REQUEST);
        }

        Optional<User> existingUser = userService.findByUsername(registrationUserDto.getUsername());
        if (existingUser.isPresent()) {
            logger.warn("User already exists: {}", registrationUserDto.getUsername());
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь с указанным именем уже существует"), HttpStatus.BAD_REQUEST);
        }

        User user = userService.createNewUser(registrationUserDto);
        logger.info("User created: {}", user.getUsername());
        return ResponseEntity.ok(new UserDto( user.getUsername(), user.getEmail()));
    }
}