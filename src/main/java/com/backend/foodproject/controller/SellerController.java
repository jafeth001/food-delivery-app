package com.backend.foodproject.controller;

import com.backend.foodproject.entity.Seller;
import com.backend.foodproject.error.ConflictException;
import com.backend.foodproject.error.GlobalException;
import com.backend.foodproject.model.UpdatePassword;
import com.backend.foodproject.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerController {
    private final SellerService sellerService;

    @GetMapping("/profile")
    public Seller sellerProfile(@AuthenticationPrincipal Seller seller) {
        return seller;
    }

    @PutMapping("/update")
    public Seller updateSellerr(@RequestParam String email, @RequestBody @AuthenticationPrincipal Seller seller) {
        return sellerService.updateSeller(seller, email);
    }

    @PutMapping("/update-password")
    public String updateSellerPassword
            (@RequestBody UpdatePassword request, @AuthenticationPrincipal Seller seller,
             @RequestParam String email) throws ConflictException, GlobalException {
        return sellerService.updateSellerPassword(email, request);
    }

    @DeleteMapping("/delete")
    public String sellerDelete(@AuthenticationPrincipal Seller seller) throws GlobalException {
        return sellerService.sellerDelete(seller);
    }

    @PutMapping("/forgot-password")
    public String sellerForgotPassword(@RequestParam String email) {
        return sellerService.sellerForgotPassword(email);
    }

    @PutMapping("/reset-password")
    private String SellerResetPassword
            (@RequestBody UpdatePassword request, @RequestParam String token, @RequestParam String email) throws ConflictException {
        return sellerService.SellerResetPassword(request,email, token);
    }
}