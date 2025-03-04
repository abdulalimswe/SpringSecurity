package com.utin.oj.service;

import com.utin.oj.domain.dto.User;
import com.utin.oj.entity.RoleEntity;
import com.utin.oj.enumeration.LoginType;

public interface UserService {
    void createUser(String firstname, String lastname, String email, String password);
    RoleEntity getRoleName(String name);
    void verifyAccountKey(String key);
    void updateLoginAttempt(String email, LoginType loginType);
}
