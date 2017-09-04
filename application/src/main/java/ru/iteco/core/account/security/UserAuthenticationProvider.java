package ru.iteco.core.account.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ru.iteco.core.account.User;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    private final Integer IS_EMPTY = -1;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = (User) authentication.getPrincipal();
        if (isValid(user)) {
            authentication.setAuthenticated(true);
            return authentication;
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UserAuthentication.class);
    }

    private boolean isValid(User user) {
        Integer userId = user.getUserId();
        Integer profileId = user.getProfileId();
        return userId > IS_EMPTY && profileId > IS_EMPTY;
    }
}
