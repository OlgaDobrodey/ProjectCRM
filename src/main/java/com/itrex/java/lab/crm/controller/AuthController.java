package com.itrex.java.lab.crm.controller;

import com.itrex.java.lab.crm.dto.UserAuthenticationDTO;
import com.itrex.java.lab.crm.dto.UserDtoLoginRoleName;
import com.itrex.java.lab.crm.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.crm.security.JwtTokenProvider;
import com.itrex.java.lab.crm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final SecurityContextLogoutHandler securityContextLogoutHandler;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody UserAuthenticationDTO request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));
            UserDtoLoginRoleName user = userService.getByLogin(request.getLogin());
            if (user == null) {
                return new ResponseEntity<>("User doesn't exists", HttpStatus.NOT_FOUND);
            }
            String token = jwtTokenProvider.createToken(request.getLogin(), user.getRoleName());
            Map<Object, Object> response = new HashMap<>();
            response.put("login", request.getLogin());
            response.put("token", token);
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (AuthenticationException | CRMProjectServiceException e) {
            return new ResponseEntity<>("Invalid email/password combination", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        securityContextLogoutHandler.logout(request, response, null);
    }

}
