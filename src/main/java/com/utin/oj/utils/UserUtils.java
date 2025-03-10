package com.utin.oj.utils;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.utin.oj.dto.User;
import com.utin.oj.entity.CredentialEntity;
import com.utin.oj.entity.RoleEntity;
import com.utin.oj.entity.UserEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

import static java.time.LocalDateTime.now;

public class UserUtils {
    public static UserEntity createUserEntity(String firstname, String lastname, String email, RoleEntity role){
        return UserEntity.builder()
                .userId(UUID.randomUUID().toString())
                .firstName(firstname)
                .lastName(lastname)
                .email(email)
                .lastLogin(now())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .mfa(false)
                .enabled(false)
                .loginAttempts(0)
                .qrCodeSecret(StringUtils.EMPTY)
                .phone(StringUtils.EMPTY)
                .bio(StringUtils.EMPTY)
                .imageUrl("https://static.vecteezy.com/system/resources/previews/005/544/718/non_2x/profile-icon-design-free-vector.jpg")
                .role(role)
                .build();


    }

    public static User fromUserEntity(UserEntity userEntity, RoleEntity role, CredentialEntity credentialEntity) {
        User user = new User();
        BeanUtils.copyProperties(userEntity,user);
        user.setLastLogin(userEntity.getLastLogin().toString());
        user.setCredentialsNonExpired(isCredentialsNonExpired(credentialEntity));
        user.setCreatedAt(userEntity.getCreatedAt().toString());
        user.setUpdatedAt(userEntity.getUpdatedAt().toString());
        user.setRole(role.getName());
        user.setAuthorities(role.getAuthorities().getValue());
        return user;
    }

    private static Boolean isCredentialsNonExpired(CredentialEntity credentialEntity) {
        return credentialEntity.getUpdatedAt().plusDays(90).isAfter(now());
    }

}
