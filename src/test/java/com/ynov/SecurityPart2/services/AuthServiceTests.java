package com.ynov.SecurityPart2.services;

import com.ynov.SecurityPart2.models.Role;
import com.ynov.SecurityPart2.models.User;
import com.ynov.SecurityPart2.security.JwtService;
import com.ynov.SecurityPart2.servicesImplem.AuthImplem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTests {

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    UserService userService;
    @Mock
    JwtService jwtService;

    @InjectMocks
    AuthImplem authImplem;

    @Test
    public void testRegisterProf (){
        User user = new User();
        String password = "password";
        user.setPassword(password);
        Role role = new Role(null, Role.RoleEnum.PROF.name());
        when(bCryptPasswordEncoder.encode(password)).thenReturn("encryptedPassword");
        when(userService.createUser(any(User.class))).thenReturn(user);

        doAnswer((InvocationOnMock invocation) -> {
            User argUser = invocation.getArgument(0);
            Role argRole = invocation.getArgument(1);
            argUser.setRoles(Collections.singletonList(argRole));
            return null;
        }).when(userService).addRoleToUser(user, role);


        User result = authImplem.register(user, role);
        assertEquals("encryptedPassword", result.getPassword());
        assertTrue(result.getRoles().contains(role));
    }

    @Test
    public void testLoginWithValidCredentials_shouldGenerateToken(){
        User user = new User();
        user.setPassword("encyptedPassword");
        String password = "password";
        when(bCryptPasswordEncoder.matches(password, user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("Token");
        String result = authImplem.login(user, password);
        assertNotNull(result);
        assertEquals("Token", result);
    }

    @Test
    public void testLoginWithInvalidCredentials_shouldNotGenerateToken(){
        User user = new User();
        user.setPassword("encyptedPassword");
        String password = "IncorrectPassword";
        when(bCryptPasswordEncoder.matches(password, user.getPassword())).thenReturn(false);
        String result = authImplem.login(user, password);
        assertNull(result);
    }

}
