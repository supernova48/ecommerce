package com.siju.ecommerce.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.siju.ecommerce.auth.dto.LoginResponse;
import com.siju.ecommerce.auth.dto.RegisterRequest;
import com.siju.ecommerce.exception.EmailAlreadyExistsException;
import com.siju.ecommerce.exception.UsernameAlreadyExistsException;
import com.siju.ecommerce.security.JwtService;
import com.siju.ecommerce.user.Role;
import com.siju.ecommerce.user.User;
import com.siju.ecommerce.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final static Logger logger = LoggerFactory.getLogger(AuthService.class);

    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistsException(request.username());
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email already exists: " + request.email());
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(Role.CUSTOMER)
                .enabled(true)
                .build();
        userRepository.save(user);
    }

    public LoginResponse login(String username, String password) {
        // Implement login logic here
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found" + username));

        logger.debug("Does Password match: {}", (passwordEncoder.matches(password, user.getPasswordHash())));
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        String accessToken = jwtService.generateToken(user);
        return new LoginResponse(accessToken);
    }

}
