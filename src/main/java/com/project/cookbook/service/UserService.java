package com.project.cookbook.service;

import com.project.cookbook.exception.UserNotFoundException;
import com.project.cookbook.model.Book;
import com.project.cookbook.model.User;
import com.project.cookbook.repository.BookRepository;
import com.project.cookbook.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.management.BadAttributeValueExpException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public void createUser(String username, String email) throws BadAttributeValueExpException {
        if (isUsernameTaken(username)) {
            throw new BadAttributeValueExpException(username);
        }
        Book book = new Book();
        User user = User.builder()
                .username(username)
                .email(email)
                .book(book)
                .points(0)
                .build();
        book.setUser(user);
        bookRepository.saveAndFlush(book);
        userRepository.saveAndFlush(user);
    }

    public User getUserById(long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElseThrow(() -> new UserNotFoundException(id));
    }

    private boolean isUsernameTaken(String username) {
        return userRepository.findAll()
                .stream()
                .map(User::getUsername)
                .anyMatch(u -> u.equals(username));
    }
}
