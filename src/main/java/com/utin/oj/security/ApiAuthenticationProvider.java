package com.utin.oj.security;


import com.utin.oj.domain.ApiAuthentication;
import com.utin.oj.domain.UserPrincipal;
import com.utin.oj.exception.ApiException;
import com.utin.oj.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.utin.oj.domain.ApiAuthentication.authenticated;

@Component
@RequiredArgsConstructor
public class ApiAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;
    private final BCryptPasswordEncoder encoder;


    //    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        var user = (UsernamePasswordAuthenticationToken) authentication;
//        var userFromDb = userDetailsService.loadUserByUsername((String) user.getPrincipal());
//        var password = (String)user.getCredentials();
//        if(password.equals(userFromDb.getPassword())) {
//            return UsernamePasswordAuthenticationToken.authenticated(userFromDb, "[PASSWORD PROTECTED]", userFromDb.getAuthorities());
//        }
//        throw new BadCredentialsException("Unable to login");
//    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var apiAuthentication = authenticationFunction.apply(authentication);
        var user = userService.getUserByEmail(apiAuthentication.getEmail());
        if(user != null) {
            var userCredential = userService.getUserCredentialById(user.getId());
//            if(userCredential.getUpdatedAt().minusDays(90).isAfter(LocalDateTime.now())) {throw new ApiException("Credentials are expired. Please reset your password");}
            if(user.isCredentialsNonExpired()) {throw new ApiException("Credentials are expired. Please reset your password");}
            var userPrincipal = new UserPrincipal(user,userCredential);
            validAccount.accept(userPrincipal);
            if(encoder.matches(apiAuthentication.getPassword(), userCredential.getPassword())){
                return authenticated(user, userPrincipal.getAuthorities());
            } else throw  new BadCredentialsException("Email or Password incorrect. Please try again..");

        } else throw new ApiException("unable to authenticate");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiAuthentication.class.isAssignableFrom(authentication);
    }

    private final Function<Authentication, ApiAuthentication> authenticationFunction = authentication -> (ApiAuthentication) authentication;

    private final Consumer<UserPrincipal> validAccount = userPrincipal -> {
        if(userPrincipal.isAccountNonLocked()) { throw new LockedException("Your account is currently locked");}
        if(userPrincipal.isEnabled()) { throw new DisabledException("Your account is currently disable");}
        if(userPrincipal.isCredentialsNonExpired()) { throw new CredentialsExpiredException("Your password has expired. Please update your password");}
        if(userPrincipal.isAccountNonExpired()) { throw new DisabledException("Your account has expired. Please contact administrator");}
    };

}
