package com.project.cookbook.service;

import com.project.cookbook.constants.UserType;
import com.project.cookbook.model.PermissionLevel;
import com.project.cookbook.model.PrincipalUser;
import com.project.cookbook.model.User;
import com.project.cookbook.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private final static long ID = 1;
    private final static String USERNAME = "USER1";
    private final static String EMAIL = "EMAIL1";
    private final static String PASSWORD = "PASS";
    private final static String ENCODED_PASSWORD = "2$njk#nklbhjll";
    private final static String NEW_PASSWORD = "PASSWORD";
    private final static String ENCODED_NEW_PASSWORD = "5%njk#njzydxll";

    @Mock
    private UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    private UserService userService;

    @Before
    public void setUp() {
        given(passwordEncoder.encode(any())).willReturn(ENCODED_PASSWORD);
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    public void shouldReturnUserById() {
        //given
        given(userRepository.findById(eq(ID))).willReturn(Optional.of(mockUser()));

        //when
        User user = userService.getUserById(ID);

        assertThat(user.getUsername()).isEqualTo(USERNAME);
    }

    @Test
    public void shouldChangePassword() {
        //given
        given(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).willReturn(true);
        given(passwordEncoder.encode(NEW_PASSWORD)).willReturn(ENCODED_NEW_PASSWORD);
        PrincipalUser principalUser = mockPrincipalUser();

        //when
        userService.changePassword(principalUser, NEW_PASSWORD, PASSWORD);

        //then
        assertThat(principalUser.getPassword()).isEqualTo(ENCODED_NEW_PASSWORD);
    }

    private PrincipalUser mockPrincipalUser() {
        return new PrincipalUser(mockUser());
    }

    private User mockUser() {
        PermissionLevel permissionLevel = new PermissionLevel();
        permissionLevel.setUserType(UserType.ROLE_USER);
        return User.builder()
                .id(ID)
                .username(USERNAME)
                .email(EMAIL)
                .password(ENCODED_PASSWORD)
                .permissionLevel(permissionLevel)
                .build();
    }
}