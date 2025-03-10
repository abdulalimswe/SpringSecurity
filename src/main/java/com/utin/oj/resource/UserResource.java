package com.utin.oj.resource;

import com.utin.oj.domain.Response;
import com.utin.oj.dtorequest.UserRequest;
import com.utin.oj.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

import static com.utin.oj.utils.RequestUtils.getResponse;
import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/user"})
public class UserResource {

    @Autowired
    private final UserService userService;



    @PostMapping("/register")
    public ResponseEntity<Response> saveUser(@RequestBody @Valid UserRequest user, HttpServletRequest request){
        userService.createUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());

        return ResponseEntity.created( getUri()).body(getResponse(request, emptyMap(), "Account Created. Check Your mail to enable your account", CREATED));

    }

    @GetMapping("/verify/account")
    public ResponseEntity<Response> verifyAccount(@RequestParam("key") String key, HttpServletRequest request){
        userService.verifyAccountKey(key);
        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Account Verified", OK));
    }

//    private final AuthenticationManager authenticationManager;
//    @PostMapping("/login")
//    public ResponseEntity<?> test(@RequestBody UserRequest user){
//        UsernamePasswordAuthenticationToken unauthenticated = UsernamePasswordAuthenticationToken.unauthenticated(user.getEmail(), user.getPassword());
//        Authentication authenticate = authenticationManager.authenticate(unauthenticated);
//        return ResponseEntity.ok().body(Map.of("user", authenticate));
//    }



    private URI getUri() {
        return URI.create("");
    }
}
