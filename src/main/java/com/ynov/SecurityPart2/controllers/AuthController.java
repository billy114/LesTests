package com.ynov.SecurityPart2.controllers;

import com.ynov.SecurityPart2.models.Role;
import com.ynov.SecurityPart2.models.User;
import com.ynov.SecurityPart2.repositories.RoleRepo;
import com.ynov.SecurityPart2.services.AuthService;
import com.ynov.SecurityPart2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;
    @Autowired
    RoleRepo roleRepo;

    private ResponseEntity<?> userExisteResponse (User entity){
        Optional<User> user = userService.getUSerByPseudo(entity.getPseudo());
        if(user.isPresent())
            return new ResponseEntity<>(
                    "pseudo existe déjà",
                    HttpStatus.CONFLICT
            );
        return null;
    }

    @PostMapping("register-prof")
    public ResponseEntity<?> profRegister(@RequestBody User entity){
        ResponseEntity<?> res = userExisteResponse(entity);
        if (res != null)
            return res;
        Optional<Role> role = roleRepo.findByRoleName(Role.RoleEnum.PROF.name());
        if(role.isEmpty())
            return new ResponseEntity<>(
                    "Une erreur est servenue !",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        return new ResponseEntity<>(
                authService.register(entity, role.get()),
                HttpStatus.CREATED
        );
    }

    @PostMapping("register-student")
    public ResponseEntity<?> studentRegister(@RequestBody User entity){
        ResponseEntity<?> res = userExisteResponse(entity);
        if (res != null)
            return res;
        Optional<Role> role = roleRepo.findByRoleName(Role.RoleEnum.STUDENT.name());
        if(role.isEmpty())
            return new ResponseEntity<>(
                    "Une erreur est servenue !",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        return new ResponseEntity<>(
                authService.register(entity, role.get()),
                HttpStatus.CREATED
        );
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request){
        String pseudo = request.get("pseudo");
        String password = request.get("password");
        Optional<User> user = userService.getUSerByPseudo(pseudo);
        if (user.isEmpty())
            return new ResponseEntity(
                    "User n'existe pas",
                    HttpStatus.NOT_FOUND
            );
        String jwt = authService.login(user.get(), password);
        if (jwt == null)
            return new ResponseEntity<>(
                    "Mot de passe incorrect",
                    HttpStatus.FORBIDDEN
            );
        return new ResponseEntity<>(
                jwt,
                HttpStatus.OK
        );
    }
}
