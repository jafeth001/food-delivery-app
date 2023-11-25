package com.backend.foodproject.controller;

import com.backend.foodproject.entity.Seller;
import com.backend.foodproject.entity.User;
import com.backend.foodproject.error.ConflictException;
import com.backend.foodproject.error.GlobalException;
import com.backend.foodproject.model.UpdatePassword;
import com.backend.foodproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public User userProfile(@AuthenticationPrincipal User user) {
        return user;
    }

    @PutMapping("/update")
    public User updateUser
            (@RequestBody @AuthenticationPrincipal User user ,@RequestParam String email) {
        return userService.updateUser(user,email);
    }

    @PutMapping("/update-password")
    public String updateUserPassword
            (@RequestBody UpdatePassword request, @AuthenticationPrincipal User user, @RequestParam String email)
            throws GlobalException, ConflictException {
        return userService.updateUserPassword(request, email);
    }

    @DeleteMapping("/delete")
    public String userDelete(@AuthenticationPrincipal User user) throws GlobalException {
        return userService.userDelete(user);
    }
    @PutMapping("/forgot-password")
    public String userForgotPassword(@RequestParam String email) {
        return userService.userForgotPassword(email);
    }

    @PutMapping("/reset-password")
    private String userResetPassword
            (@RequestBody UpdatePassword request, @RequestParam String token, @RequestParam String email) throws ConflictException {
        return userService.userResetPassword(request,email, token);
    }
}
