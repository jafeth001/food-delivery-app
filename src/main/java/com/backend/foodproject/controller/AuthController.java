package com.backend.foodproject.controller;

import com.backend.foodproject.error.ConflictException;
import com.backend.foodproject.error.GlobalException;
import com.backend.foodproject.model.AuthenticationRequest;
import com.backend.foodproject.model.AuthenticationResponse;
import com.backend.foodproject.model.RegistrationRequest;
import com.backend.foodproject.service.AuthSellerService;
import com.backend.foodproject.service.AuthUserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthUserService authUserService;
    private final AuthSellerService authSellerService;
    /***** user registration and login ***/

    @PostMapping("/user/register")
    public ResponseEntity<String> userRegister(@RequestBody RegistrationRequest request)
            throws MessagingException, GlobalException, ConflictException {
        log.info("user registration :{}", request);
        return ResponseEntity.ok(authUserService.userRegister(request));
    }

    @PutMapping("/user/verify-account")
    public ResponseEntity<String> verifUseryAccount(@RequestParam String email,
                                                    @RequestParam String token) {
        log.info("user account verification");
        return ResponseEntity.ok(authUserService.verifUseryAccount(email, token));
    }
    @PutMapping("/user/regenerate-token")
    public ResponseEntity<String> regenerateUserToken(@RequestParam String email){
        return ResponseEntity.ok(authUserService.regenerateUserToken(email));
    }

    @PostMapping("/user/login")
    public ResponseEntity<AuthenticationResponse> userAuthenticate(@RequestBody AuthenticationRequest request)
            throws GlobalException, ConflictException {
        log.info("user login :{}", request);
        return ResponseEntity.ok(authUserService.userAuthenticate(request));
    }

         /***** seller registration and login ***/
    @PostMapping("/seller/register")
    public ResponseEntity<String> sellerRegister(@RequestBody RegistrationRequest request)
            throws ConflictException {
        log.info("seller registration :{}", request);
        return ResponseEntity.ok(authSellerService.sellerRegister(request));
    }

    @PutMapping("/seller/verify-account")
    public ResponseEntity<String> verifySellerAccount(@RequestParam String email,
                                                      @RequestParam String token) {
        log.info("seller account verification");
        return ResponseEntity.ok(authSellerService.verifySellerAccount(email, token));
    }

    @PutMapping("/seller/regenerate-token")
    public ResponseEntity<String> regenerateSellerToken(@RequestParam String email) {
        return  ResponseEntity.ok(authSellerService.regenerateSellerToken(email));
    }

    @PostMapping("/seller/login")
    public ResponseEntity<AuthenticationResponse> sellerAuthenticate(@RequestBody AuthenticationRequest request)
            throws ConflictException, GlobalException {
        log.info("seller login :{}", request);
        return ResponseEntity.ok(authSellerService.sellerAuthenticate(request));
    }
}
