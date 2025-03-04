package com.utin.oj.service.impl;

import com.utin.oj.Repository.ConfirmationRepository;
import com.utin.oj.Repository.CredentialRepository;
import com.utin.oj.Repository.RoleRepository;
import com.utin.oj.Repository.UserRepository;
import com.utin.oj.cache.CacheStore;
import com.utin.oj.domain.RequestContext;
import com.utin.oj.domain.dto.User;
import com.utin.oj.entity.ConfirmationEntity;
import com.utin.oj.entity.CredentialEntity;
import com.utin.oj.entity.RoleEntity;
import com.utin.oj.entity.UserEntity;
import com.utin.oj.enumeration.Authority;
import com.utin.oj.enumeration.EventType;
import com.utin.oj.enumeration.LoginType;
import com.utin.oj.event.UserEvent;
import com.utin.oj.exception.ApiException;
import com.utin.oj.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

import static com.utin.oj.utils.UserUtils.createUserEntity;
import static java.time.LocalDateTime.now;

@Service
@Transactional(rollbackOn =  Exception.class)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CredentialRepository credentialRepository;
    private final ConfirmationRepository confirmationRepository;
    //private final BCryptPasswordEncoder encoder;
    private final CacheStore<String, Integer> userCache;
    private final ApplicationEventPublisher publisher;
    @Override
    public void createUser(String firstname, String lastname, String email, String password){
        var userEntity = userRepository.save(createNewUser(firstname, lastname,email));
        var credentialEntity = new CredentialEntity(userEntity, password);
        credentialRepository.save(credentialEntity);

        var confirmationEntity = new ConfirmationEntity(userEntity);
        confirmationRepository.save(confirmationEntity);
        publisher.publishEvent(new UserEvent(userEntity, EventType.REGISTRATION, Map.of("key",confirmationEntity.getKey())));
    }

    public RoleEntity getRoleName(String name) {
        var role = roleRepository.findByNameIgnoreCase(name);
        return role.orElseThrow(() -> new ApiException("Role Not found"));
    }

    @Override
    public void verifyAccountKey(String key){
        var confirmationEntity = getUserConfirmation(key);
        var userEntity = getUserEntityByEmail(confirmationEntity.getUserEntity().getEmail());
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
        confirmationRepository.delete(confirmationEntity);
    }

    @Override
    public void updateLoginAttempt(String email, LoginType loginType) {
        var userEntity = getUserEntityByEmail(email);
        RequestContext.setUserId(userEntity.getId());

        switch(loginType){
            case LOGIN_ATTEMPT -> {
                if(userCache.get(userEntity.getEmail()) == null) {
                    userEntity.setLoginAttempts(0);
                    userEntity.setAccountNonExpired(true);
                }
                userEntity.setLoginAttempts(userEntity.getLoginAttempts()+1);
                userCache.put(userEntity.getEmail(), userEntity.getLoginAttempts());

                if(userCache.get(userEntity.getEmail()) > 5){
                    userEntity.setAccountNonLocked(false);
                }
            }
            case LOGIN_SUCCESS -> {
                userEntity.setAccountNonLocked(true);
                userEntity.setLoginAttempts(0);
                userEntity.setLastLogin(now());
                userCache.evict(userEntity.getEmail());
            }
        }
        userRepository.save(userEntity);
    }


    private UserEntity getUserEntityByEmail(String email) {
        var userByEmail = userRepository.findByEmailIgnoreCase(email);
        return  userByEmail.orElseThrow(() -> new ApiException("User not found"));
    }

    private ConfirmationEntity getUserConfirmation(String key) {
        return confirmationRepository.findByKey(key).orElseThrow(() -> new ApiException("Confirmation Key not found"));
    }

    private UserEntity createNewUser(String firstname, String lastname, String email){
        var role = getRoleName(Authority.USER.name());
        return createUserEntity(firstname,lastname,email,role);
    }
}
