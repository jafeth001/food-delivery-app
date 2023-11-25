package com.backend.foodproject.service;

import com.backend.foodproject.config.JwtService;
import com.backend.foodproject.entity.*;
import com.backend.foodproject.error.ConflictException;
import com.backend.foodproject.error.GlobalException;
import com.backend.foodproject.model.AuthenticationRequest;
import com.backend.foodproject.model.AuthenticationResponse;
import com.backend.foodproject.model.RegistrationRequest;
import com.backend.foodproject.reposiory.SellerRepository;
import com.backend.foodproject.reposiory.TokenRepository;
import com.backend.foodproject.utils.EmailUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthSellerService {
    private final SellerRepository sellerRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailUtil emailUtil;

    public String sellerRegister(RegistrationRequest request) throws ConflictException {
        Seller regSeller = sellerRepository.findByEmail(request.getEmail());
        if (regSeller == null) {
            Seller seller = Seller
                    .builder()
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.SELLER)
                    .active(false)
                    .build();
            sellerRepository.save(seller);
            var token = jwtService.generateToken(seller);
            try {
                emailUtil.sendSellerTokenEmail(request.getEmail(), token);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            extractdSellerToken(seller, token);
            return ("Account created check your email " + request.getEmail() +
                    " to activate account " + "within 24Hrs");
        }
        throw new ConflictException("User with this email " + request.getEmail() + " exists");
    }

    private void extractdSellerToken(Seller seller, String token) {
        Token tokens = Token.builder()
                .token(token)
                .tokenType(TokenType.BEARER)
                .seller(seller)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(tokens);
    }

    public String verifySellerAccount(String email, String token) {
        Seller activeUser = sellerRepository.findByEmail(email);
        activeUser.setActive(true);
        sellerRepository.save(activeUser);
        return "account verified.... you can login";
    }

    public AuthenticationResponse sellerAuthenticate(AuthenticationRequest request)
            throws ConflictException, GlobalException {
        Seller loginSeller = sellerRepository.findByEmail(request.getEmail());
        if (loginSeller != null && passwordEncoder.matches(request.getPassword(), loginSeller.getPassword())) {
            if (loginSeller.isActive()) {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
                var seller = sellerRepository.findByEmail(request.getEmail());
                var token = jwtService.generateToken(seller);
                revokeAllSellerTokens(seller);
                extractdSellerToken(seller, token);
                return AuthenticationResponse
                        .builder()
                        .token(token)
                        .message(request.getEmail() + " login Successfully")
                        .role(Role.SELLER)
                        .build();
            }
            throw new ConflictException("account not activated");
        }
        throw new GlobalException("incorrect username or password");
    }

    public void revokeAllSellerTokens(Seller seller) {
        var validTokens = tokenRepository.findAllValidTokensBySelller(seller.getId());
        if (validTokens.isEmpty()) {
            return;
        }
        validTokens.forEach(t -> {
            t.setRevoked(true);
            t.setExpired(true);
        });
        tokenRepository.saveAll(validTokens);

    }

    public String regenerateSellerToken(String email) {
        Seller seller = sellerRepository.findByEmail(email);
        String token = jwtService.generateToken(seller);
        try {
            emailUtil.sendSellerTokenEmail(email, token);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        sellerRepository.save(seller);
        return ("check your email " + email + " to activate account " + "within 24Hrs");
    }
}