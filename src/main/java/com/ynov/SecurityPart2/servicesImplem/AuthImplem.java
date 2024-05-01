package com.ynov.SecurityPart2.servicesImplem;

import com.ynov.SecurityPart2.models.Role;
import com.ynov.SecurityPart2.models.User;
import com.ynov.SecurityPart2.security.JwtService;
import com.ynov.SecurityPart2.services.AuthService;
import com.ynov.SecurityPart2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthImplem implements AuthService {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserService userService;
    @Autowired
    JwtService jwtService;

    @Override
    public String login(User user, String password) {
        if(bCryptPasswordEncoder.matches(password, user.getPassword()))
            return jwtService.generateToken(user);
        return null;
    }

    @Override
    public User register(User entity, Role role) {
        String passwordEncoded = bCryptPasswordEncoder.encode(entity.getPassword());
        entity.setPassword(passwordEncoded);
        userService.addRoleToUser(entity, role);
        return userService.createUser(entity);
    }
}
