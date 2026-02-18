package dev.marco.taskapi.domain.service;

import dev.marco.taskapi.domain.model.User;
import dev.marco.taskapi.domain.provider.TokenProvider;
import dev.marco.taskapi.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    public String register(String username, String email, String password) {
    	User userToSave = new User(
                null, 
                username, 
                passwordEncoder.encode(password), 
                email, 
                "USER"
            );
    	User savedUser = userRepository.save(userToSave);
    	
        return tokenProvider.generateToken(savedUser);
    }

    public String authenticate(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        UserDetails user = (UserDetails) authentication.getPrincipal();
        return tokenProvider.generateToken(user);
    }
}
