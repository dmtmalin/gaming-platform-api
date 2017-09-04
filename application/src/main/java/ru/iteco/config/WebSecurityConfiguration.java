package ru.iteco.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.iteco.core.account.security.UserAuthenticationProvider;
import ru.iteco.core.account.security.UserAuthenticationRequestFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserAuthenticationProvider userAuthenticationProvider;
    private final UserAuthenticationRequestFilter userAuthenticationRequestFilter;

    @Autowired
    public WebSecurityConfiguration(UserAuthenticationProvider userAuthenticationProvider,
                                    UserAuthenticationRequestFilter userAuthenticationRequestFilter) {
        this.userAuthenticationProvider = userAuthenticationProvider;
        this.userAuthenticationRequestFilter = userAuthenticationRequestFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable()
                .addFilterBefore(userAuthenticationRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .anyRequest()
                .fullyAuthenticated()
                .and()
                .authorizeRequests()
                .antMatchers("/attach/**")
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(userAuthenticationProvider);
    }
}
