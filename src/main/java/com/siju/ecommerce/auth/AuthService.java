package com.siju.ecommerce.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.siju.ecommerce.auth.dto.RegisterRequest;
import com.siju.ecommerce.exception.EmailAlreadyExistsException;
import com.siju.ecommerce.exception.UsernameAlreadyExistsException;
import com.siju.ecommerce.user.Role;
import com.siju.ecommerce.user.User;
import com.siju.ecommerce.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistsException(request.username());
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
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
        
}
