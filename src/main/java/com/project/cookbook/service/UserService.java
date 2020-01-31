package com.project.cookbook.service;

import com.project.cookbook.GeneratedModels;
import com.project.cookbook.converter.UserConverter;
import com.project.cookbook.exception.UserNotFoundException;
import com.project.cookbook.exception.WrongPasswordException;
import com.project.cookbook.model.PrincipalUser;
import com.project.cookbook.model.User;
import com.project.cookbook.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserById(long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElseThrow(() -> new UserNotFoundException(id));
    }

    public GeneratedModels.UsersResponse getTopUsers(int resultSize) {
        List<GeneratedModels.UserSchema> users = userRepository.findAll().stream()
                .sorted(Comparator.comparingLong(User::getPoints).reversed())
                .map(UserConverter::convert)
                .limit(resultSize)
                .collect(Collectors.toList());

        return GeneratedModels.UsersResponse.newBuilder()
                .addAllUsers(users)
                .build();
    }

    public GeneratedModels.UserSchema getUserProfile(long id) {
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional
                .orElseThrow(() -> new UserNotFoundException(id));
        return UserConverter.convert(user);
    }

    public void changePassword(PrincipalUser principalUser, String newPassword, String oldPassword) {
        User user = principalUser.getUser();
        String encodedOld = passwordEncoder.encode(oldPassword);
        String userPassword = user.getPassword();
        boolean passwordCorrect = passwordEncoder.matches(oldPassword, user.getPassword());
        if (passwordCorrect) {
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
            userRepository.saveAndFlush(user);
        } else {
            throw new WrongPasswordException(user);
        }
    }

    public void deleteUserById(PrincipalUser principalUser) {
        User user = principalUser.getUser();
        userRepository.delete(user);
    }

    public void updateUserPoints(User user, long pointsAmount) {
        user.addPoints(pointsAmount);
        userRepository.saveAndFlush(user);
    }
}
