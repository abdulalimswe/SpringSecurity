package com.utin.oj.utils;

import com.utin.oj.entity.RoleEntity;
import com.utin.oj.entity.UserEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

import static java.time.LocalDateTime.now;

public class UserUtils {
    public static UserEntity createUserEntity(String firstname, String lastname, String email, RoleEntity role){
        return UserEntity.builder()
                .userId(UUID.randomUUID().toString())
                .firstname(firstname)
                .lastname(lastname)
                .email(email)
                .lastLogin(now())
                .accountNonExpired(true)
                .AccountNonLocked(true)
                .mfa(false)
                .enabled(false)
                .loginAttemps(0)
                .qrCodeSecret(StringUtils.EMPTY)
                .phone(StringUtils.EMPTY)
                .bio(StringUtils.EMPTY)
                .imageUrl("https://static.vecteezy.com/system/resources/previews/005/544/718/non_2x/profile-icon-design-free-vector.jpg")
                .role(role)
                .build();


    }
}
