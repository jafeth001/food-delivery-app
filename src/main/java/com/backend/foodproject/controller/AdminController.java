package com.backend.foodproject.controller;

import com.backend.foodproject.entity.Seller;
import com.backend.foodproject.entity.User;
import com.backend.foodproject.entity.WebOrder;
import com.backend.foodproject.error.GlobalException;
import com.backend.foodproject.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/users/list")
    public List<User> userList(@AuthenticationPrincipal Seller seller) throws GlobalException {
        return adminService.userList(seller);
    }

    @GetMapping("/user")
    public User userById(@AuthenticationPrincipal Seller seller, @RequestParam Long id) throws GlobalException {
        return adminService.userById(seller, id);
    }

    @PutMapping("/deactivate")
    public String deactivateUser
            (@AuthenticationPrincipal Seller seller, User user, @RequestParam Long id) {
        return adminService.deactivateUser(seller, user, id);
    }
    @PutMapping("/activate")
    public String activateUser
            (@AuthenticationPrincipal Seller seller, User user, @RequestParam Long id) {
        return adminService.activateUser(seller, user, id);
    }

    @GetMapping("/order/get/all")
    public List<WebOrder> getorders(@AuthenticationPrincipal Seller seller) throws GlobalException {
        return adminService.getorders(seller);
    }

    @GetMapping("/order/get")
    public WebOrder getOrder
            (@RequestParam Long id, @AuthenticationPrincipal Seller seller) throws GlobalException {
        return adminService.getOrder(id, seller);
    }
}
