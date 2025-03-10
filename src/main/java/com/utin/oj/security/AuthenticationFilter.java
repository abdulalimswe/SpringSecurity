package com.utin.oj.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utin.oj.domain.ApiAuthentication;
import com.utin.oj.domain.Response;
import com.utin.oj.dto.User;
import com.utin.oj.dtorequest.LoginRequest;
import com.utin.oj.entity.CredentialEntity;
import com.utin.oj.service.JwtService;
import com.utin.oj.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import static com.fasterxml.jackson.core.JsonParser.Feature.AUTO_CLOSE_SOURCE;
import static com.utin.oj.enumeration.LoginType.*;
import static com.utin.oj.enumeration.TokenType.ACCESS;
import static com.utin.oj.enumeration.TokenType.REFRESH;
import static com.utin.oj.utils.RequestUtils.getResponse;
import static com.utin.oj.utils.RequestUtils.handleErrorResponse;
import static java.util.Map.of;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    private final UserService userService;
    private final JwtService jwtService;
    protected AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, JwtService jwtService) {
        super(new AntPathRequestMatcher("/user/login", POST.name()), authenticationManager);
        this.userService = userService;
        this.jwtService =  jwtService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        try {
            var user = new ObjectMapper().configure(AUTO_CLOSE_SOURCE, true).readValue(request.getInputStream(), LoginRequest.class);
            userService.updateLoginAttempt(user.getEmail(), LOGIN_ATTEMPT);
            var authentication = ApiAuthentication.unauthenticated(user.getEmail(), user.getPassword());
            return getAuthenticationManager().authenticate(authentication);
        } catch (Exception exception){
            log.error(exception.getMessage());
            handleErrorResponse(request, response, exception);
            return  null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
       var user = (User) authentication.getPrincipal();
       userService.updateLoginAttempt(user.getEmail(), LOGIN_SUCCESS);
       var httpResponse = user.isMfa() ? sendQrCode(request, user) : sendResponse(request, response, user);
       response.setContentType(APPLICATION_JSON_VALUE);
       response.setStatus(OK.value());
       var out = response.getOutputStream();
       var mapper = new ObjectMapper();
       mapper.writeValue(out, httpResponse);
       out.flush();
    }

    private Response sendResponse(HttpServletRequest request, HttpServletResponse response, User user) {
        jwtService.addCookie(response, user, ACCESS);
        jwtService.addCookie(response, user, REFRESH);
        return getResponse(request, of("user", user), "Login Success", OK);
    }

    private Response sendQrCode(HttpServletRequest request, User user) {
        return  getResponse(request, of("user", user),"Please enter QR code", OK);
    }
}
