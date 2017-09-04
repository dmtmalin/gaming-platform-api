package ru.iteco.core.account.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.iteco.core.account.UserDetailsImpl;

import java.util.Collection;

public class UserAuthentication implements Authentication {
    private final UserDetails userDetails;
    private boolean isAuthenticated;

    public UserAuthentication(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return userDetails.getAuthorities();
    }

    @Override
    public Object getDetails() {
        return userDetails;
    }

    @Override
    public Object getPrincipal() {
        UserDetailsImpl userDetails = (UserDetailsImpl) this.userDetails;
        return userDetails.getUser();
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        this.isAuthenticated = b;
    }

    @Override
    public String getName() {
        return userDetails.getUsername();
    }
}
