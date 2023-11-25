package com.backend.foodproject.service;

import com.backend.foodproject.config.JwtService;
import com.backend.foodproject.entity.Role;
import com.backend.foodproject.entity.Token;
import com.backend.foodproject.entity.TokenType;
import com.backend.foodproject.entity.User;
import com.backend.foodproject.error.ConflictException;
import com.backend.foodproject.error.GlobalException;
import com.backend.foodproject.model.AuthenticationRequest;
import com.backend.foodproject.model.AuthenticationResponse;
import com.backend.foodproject.model.RegistrationRequest;
import com.backend.foodproject.reposiory.TokenRepository;
import com.backend.foodproject.reposiory.UserRepository;
import com.backend.foodproject.utils.EmailUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailUtil emailUtil;

    public String userRegister(RegistrationRequest request) throws GlobalException, ConflictException {
        User regUser = userRepository.findByEmail(request.getEmail());
        if (regUser == null) {
            User user = User
                    .builder()
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .active(false)
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
            var token = jwtService.generateToken(user);
            try {
                emailUtil.sendUserTokenEmail(request.getEmail(), token);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            extractedUserTokens(user, token);
            return ("Account created check your email " + request.getEmail() +
                    " to activate account " + "within 24Hrs");
        }
        throw new ConflictException("User with this email " + request.getEmail() + " exists");
    }

    private void extractedUserTokens(User user, String token) {
        Token tokens = Token.builder()
                .token(token)
                .tokenType(TokenType.BEARER)
                .user(user)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(tokens);
    }

    public String verifUseryAccount(String email, String token) {
        User activeUser = userRepository.findByEmail(email);
        activeUser.setActive(true);
        userRepository.save(activeUser);
        return "account verified... you can login";
    }

    public AuthenticationResponse userAuthenticate(AuthenticationRequest request)
            throws GlobalException, ConflictException {
        User activeUser = userRepository.findByEmail(request.getEmail());
        if (activeUser != null && passwordEncoder.matches(request.getPassword(), activeUser.getPassword())) {
            if (activeUser.isActive()) {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );
                var user = userRepository.findByEmail(request.getEmail());
                var token = jwtService.generateToken(user);
                revokeValidAllUserTokens(user);
                extractedUserTokens(user, token);
                return AuthenticationResponse.builder()
                        .token(token)
                        .message(request.getEmail() + " login successfully")
                        .role(Role.USER)
                        .build();
            }
            throw new ConflictException("account not active");

        }
        throw new GlobalException("incorrect username or password");
    }

    public void revokeValidAllUserTokens(User user) {
        var validToken = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validToken.isEmpty()) {
            return;
        }
        validToken.forEach(t -> {
            t.setRevoked(true);
            t.setExpired(true);
        });
    }

    public String regenerateUserToken(String email) {
        User user = userRepository.findByEmail(email);
        String token = jwtService.generateToken(user);
        try {
            emailUtil.sendUserTokenEmail(email, token);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        userRepository.save(user);
        return ("check your email " + email + " to activate account " + "within 24Hrs");
    }
}
