package com.utin.oj.dtorequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest {
    @NotEmpty(message = "Email cant be empty or null")
    @Email(message = "invalid mail address")
    private String email;
    @NotEmpty(message = "Password cant be empty or null")
    private String password;
}
