package ru.iteco.core.account.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.iteco.core.account.User;
import ru.iteco.core.account.UserToUserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UserAuthenticationRequestFilter extends OncePerRequestFilter {

    private final UserToUserDetails userToUserDetails;

    @Autowired
    public UserAuthenticationRequestFilter(UserToUserDetails userToUserDetails) {
        this.userToUserDetails = userToUserDetails;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Integer userId = request.getIntHeader("user-id");
        Integer profileId = request.getIntHeader("profile-id");
        User user = User.valueOf(userId, profileId);
        UserDetails userDetails = userToUserDetails.convert(user);
        UserAuthentication userAuthentication = new UserAuthentication(userDetails);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
        filterChain.doFilter(request, response);
    }
}
