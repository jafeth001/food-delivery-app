package com.backend.foodproject.controller;

import com.backend.foodproject.entity.Cart;
import com.backend.foodproject.entity.User;
import com.backend.foodproject.error.GlobalException;
import com.backend.foodproject.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/add")
    public Cart addCart
            (@RequestBody Cart cart,
             @AuthenticationPrincipal User user,
             @RequestParam Long id, @RequestParam String email) throws GlobalException {
        return cartService.addCart(cart, user, id,email);
    }

    @GetMapping("/get/all")
    public List<Cart> getCartList(@AuthenticationPrincipal User user) throws GlobalException {
        return cartService.getCartList(user);

    }

    @GetMapping("/get")
    public Cart getCart(@AuthenticationPrincipal User user, @RequestParam Long id) throws GlobalException {
        return cartService.getCart(user, id);

    }

    @PutMapping("/update")
    public Cart updateQuantity
            (@RequestBody Cart cart,
             @AuthenticationPrincipal User user,
             @RequestParam Long id) {
        return cartService.updateQuantity(cart, user, id);

    }

    @DeleteMapping("/delete")
    public String removeCart(@AuthenticationPrincipal User user, @RequestParam Long id) {
        return cartService.removeCart(user, id);
    }
}