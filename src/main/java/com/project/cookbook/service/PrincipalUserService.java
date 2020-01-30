package com.project.cookbook.service;

import com.project.cookbook.exception.UserNotFoundException;
import com.project.cookbook.model.PrincipalUser;
import com.project.cookbook.model.User;
import com.project.cookbook.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PrincipalUserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UserNotFoundException(usernameOrEmail));

        return new PrincipalUser(user);
    }

    public UserDetails loadById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return new PrincipalUser(user);
    }
}
