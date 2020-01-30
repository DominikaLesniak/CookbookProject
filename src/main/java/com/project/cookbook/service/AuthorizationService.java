package com.project.cookbook.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.project.cookbook.GeneratedModels;
import com.project.cookbook.constants.UserType;
import com.project.cookbook.exception.PermissionLevelNotFoundException;
import com.project.cookbook.model.Book;
import com.project.cookbook.model.PermissionLevel;
import com.project.cookbook.model.User;
import com.project.cookbook.repository.BookRepository;
import com.project.cookbook.repository.PermissionLevelRepository;
import com.project.cookbook.repository.UserRepository;
import com.project.cookbook.security.JwtTokenProvider;
import com.project.cookbook.utils.InOutService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Service
@AllArgsConstructor
public class AuthorizationService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PermissionLevelRepository permissionLevelRepository;

    public ResponseEntity<?> authenticateUser(GeneratedModels.LoginRequest loginRequest) throws InvalidProtocolBufferException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(InOutService.write(GeneratedModels.JwtAuthenticationResponse.newBuilder()
                .setAccessToken(jwt)
                .setTokenType("BEARER")
                .build()));
    }

    public ResponseEntity signUp(GeneratedModels.SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity("Username is already taken!",
                    HttpStatus.BAD_REQUEST);
        } else if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity("Email Address already in use!",
                    HttpStatus.BAD_REQUEST);
        }

        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        PermissionLevel permissionLevel = permissionLevelRepository.findByUserType(UserType.ROLE_USER)
                .orElseThrow(() -> new PermissionLevelNotFoundException(UserType.ROLE_USER));
        Book book = new Book();
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .book(book)
                .password(encodedPassword)
                .points(0)
                .permissionLevel(permissionLevel)
                .build();

        book.setUser(user);
        bookRepository.save(book);
        userRepository.saveAndFlush(user);
        bookRepository.flush();

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(user.getUsername()).toUri();

        return ResponseEntity.created(location).body("User registered successfully");
    }
}
