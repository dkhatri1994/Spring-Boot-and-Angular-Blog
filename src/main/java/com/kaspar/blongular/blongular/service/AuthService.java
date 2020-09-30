package com.kaspar.blongular.blongular.service;

import com.kaspar.blongular.blongular.dto.LoginRequest;
import com.kaspar.blongular.blongular.dto.RegisterRequest;
import com.kaspar.blongular.blongular.repository.UserRepository;
import com.kaspar.blongular.blongular.model.User;
import com.kaspar.blongular.blongular.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    public Optional<org.springframework.security.core.userdetails.User> getCurrentUser() {

         org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder
                 .getContext().getAuthentication().getPrincipal();

         return Optional.of(principal);

    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    public void signup(RegisterRequest registerRequest) {

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodePassword(registerRequest.getPassword()));

        userRepository.save(user);

    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public String login(LoginRequest loginRequest) {

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        return jwtProvider.generateToken(authenticate);

    }
}
