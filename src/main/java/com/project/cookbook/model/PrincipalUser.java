package com.project.cookbook.model;

import com.project.cookbook.constants.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static java.util.Collections.singletonList;

public class PrincipalUser implements UserDetails {
    private User user;
    private Collection<? extends GrantedAuthority> authorities;

    public PrincipalUser(User user) {
        this.user = user;

        List<GrantedAuthority> userAuthorities = singletonList(new SimpleGrantedAuthority(user.getPermissionLevel()
                .getUserType().name()));
        authorities = userAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public long getId() {
        return user.getId();
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean isAdmin() {
        return user.getPermissionLevel().getUserType().equals(UserType.ROLE_ADMIN);
    }
}
