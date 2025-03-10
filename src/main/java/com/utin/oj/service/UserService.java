package com.utin.oj.service;

import com.utin.oj.dto.User;
import com.utin.oj.entity.CredentialEntity;
import com.utin.oj.entity.RoleEntity;
import com.utin.oj.enumeration.LoginType;

public interface UserService {
    void createUser(String firstname, String lastname, String email, String password);
    RoleEntity getRoleName(String name);
    void verifyAccountKey(String key);
    void updateLoginAttempt(String email, LoginType loginType);
    User getUserByEmail(String email);
    CredentialEntity getUserCredentialById(Long id);
    User getUserByUserId(String userId);
}
