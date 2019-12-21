package com.project.cookbook.service;

import com.project.cookbook.model.Book;
import com.project.cookbook.model.User;
import com.project.cookbook.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.management.BadAttributeValueExpException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void createUser(String username, String email) throws BadAttributeValueExpException {
        if (isUsernameTaken(username)) {
            throw new BadAttributeValueExpException(username);
        }
        User user = User.builder()
                .username(username)
                .email(email)
                .book(new Book())
                .points(0)
                .build();
        userRepository.save(user);
    }

    public User getUserById(long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);
    }

    private boolean isUsernameTaken(String username) {
        return userRepository.findAll()
                .stream()
                .map(User::getUsername)
                .anyMatch(u -> u.equals(username));
    }
}
