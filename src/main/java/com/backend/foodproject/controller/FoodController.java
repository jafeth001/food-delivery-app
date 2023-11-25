package com.backend.foodproject.controller;

import com.backend.foodproject.entity.Food;
import com.backend.foodproject.entity.Seller;
import com.backend.foodproject.error.GlobalException;
import com.backend.foodproject.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
public class FoodController {
    private final FoodService foodService;

    @PostMapping("/add")
    public String addFood
            (@RequestBody Food food, @AuthenticationPrincipal Seller seller, @RequestParam Long id) {
        return foodService.addFood(food, seller, id);
    }

    @GetMapping("/get/all")
    public List<Food> getFoodLists() throws GlobalException {
        return foodService.getFoodLists();
    }

    @GetMapping("/get")
    public Food getFood(@RequestParam Long id) throws GlobalException {
        return foodService.getFood(id);
    }

    @GetMapping("/price")
    public List<Food> getFoodPrice
            (@RequestParam double minprice, @RequestParam double maxprice) throws GlobalException {
        return foodService.getFoodPrice(minprice, maxprice);
    }

    @PutMapping("/update")
    public String updateFood
            (@RequestBody Food food, @AuthenticationPrincipal Seller seller, @RequestParam Long id) throws GlobalException {
        return foodService.updateFood(id, food, seller);
    }

    @DeleteMapping("/delete")
    public String deleteFood
            (@AuthenticationPrincipal Seller seller, @RequestParam Long id) throws GlobalException {
        return foodService.deleteFood(id, seller);
    }
}
