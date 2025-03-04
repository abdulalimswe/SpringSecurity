package com.utin.oj.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.utin.oj.entity.RoleEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private Long createdBy;
    private Long updatedBy;
    private String userId;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String bio;
    private String imageUrl;
    private String qrCodeImageUri;
    private String qrCodeSecret;
    private String lastLogin;
    private String createdAt;
    private String updatedAt;
    private String role;
    private String authorities;
    private Boolean accountNonExpired;
    private Boolean AccountNonLocked;
    private Boolean credentialsNonExpired;
    private Boolean enabled;
    private Boolean mfa;


}
