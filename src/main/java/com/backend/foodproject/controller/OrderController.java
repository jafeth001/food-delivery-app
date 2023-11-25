package com.backend.foodproject.controller;

import com.backend.foodproject.entity.Food;
import com.backend.foodproject.entity.Seller;
import com.backend.foodproject.entity.User;
import com.backend.foodproject.entity.WebOrder;
import com.backend.foodproject.error.GlobalException;
import com.backend.foodproject.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/request")
    public String requestWebOrder
            (@RequestParam Long id, @RequestParam String email, @RequestBody WebOrder webOrder, User user) throws GlobalException {
        return orderService.requestWebOrder(id, email, webOrder, user);
    }

    @GetMapping("/get/all")
    public List<WebOrder> getorders(@AuthenticationPrincipal User user) throws GlobalException {
        return orderService.getorders(user);
    }

    @GetMapping("/get")
    public WebOrder getOrder
            (@RequestParam Long id, @AuthenticationPrincipal User user) throws GlobalException {
        return orderService.getOrder(id, user);
    }

    @PutMapping("/confirm")
    public WebOrder confirmOrder
            (@RequestParam Long id, @AuthenticationPrincipal User user) throws GlobalException {
        return orderService.confirmOrder(id,user);
    }
    @PutMapping("/deliver")
    public WebOrder deliveredOrder
            (@RequestParam Long id, @AuthenticationPrincipal User user) throws GlobalException {
        return orderService.deliveredOrder(id,user);
    }
    @DeleteMapping("/delete")
    public String deleteOrder
            (@RequestParam Long id, @AuthenticationPrincipal User user) throws GlobalException {
        return orderService.deleteOrder(user, id);
    }
}