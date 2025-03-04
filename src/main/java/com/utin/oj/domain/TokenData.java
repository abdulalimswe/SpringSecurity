package com.utin.oj.domain;

import com.utin.oj.domain.dto.User;
import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Builder
@Getter
@Setter
public class TokenData {
    private User user;
    private Claims claims; //jjwt
    private boolean valid;
    private List<GrantedAuthority> authorities;
}
