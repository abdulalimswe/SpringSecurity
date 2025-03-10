package com.utin.oj.service;

import com.utin.oj.Repository.CredentialRepository;
import com.utin.oj.Repository.UserRepository;
import com.utin.oj.entity.CredentialEntity;
import com.utin.oj.entity.RoleEntity;
import com.utin.oj.entity.UserEntity;
import com.utin.oj.enumeration.Authority;
import com.utin.oj.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private CredentialRepository credentialRepository;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    @DisplayName("Test find user by ID")
    public void getUserByUserIdTest(){

        var userEntity = new UserEntity();
        userEntity.setFirstName("Utin");
        userEntity.setId(1L);
        userEntity.setUserId("1");
        userEntity.setCreatedAt(LocalDateTime.of(1990, 11, 1,1,11,11));
        userEntity.setUpdatedAt(LocalDateTime.of(1990, 11, 1,1,11,11));
        userEntity.setLastLogin(LocalDateTime.of(1990, 11, 1,1,11,11));

        var roleEntity = new RoleEntity("USER", Authority.USER);
        userEntity.setRole(roleEntity);


        var credentialEntity = new CredentialEntity();
        credentialEntity.setUpdatedAt(LocalDateTime.of(1990, 11, 1,1,11,11));
        credentialEntity.setPassword("Pass123");
        credentialEntity.setUserEntity(userEntity);

        when(userRepository.findUserByUserId("1")).thenReturn(Optional.of(userEntity));
        when(credentialRepository.getCredentialByUserEntityId(1L)).thenReturn(Optional.of(credentialEntity));


        //Act - When
        var userByUserId = userServiceImpl.getUserByUserId("1");

        //assert - Then
        assertThat(userByUserId.getFirstName()).isEqualTo(userEntity.getFirstName());
        assertThat(userByUserId.getUserId()).isEqualTo("1");

    }
}
