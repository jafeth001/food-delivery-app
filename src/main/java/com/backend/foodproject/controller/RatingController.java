package com.backend.foodproject.controller;

import com.backend.foodproject.entity.Rating;
import com.backend.foodproject.entity.Seller;
import com.backend.foodproject.error.GlobalException;
import com.backend.foodproject.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rating")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;
    @PostMapping("/add")
    public String addRating
            (@RequestBody Rating rating, @AuthenticationPrincipal Seller seller, @RequestParam Long id){
        return ratingService.addRating(seller,rating,id);
    }
    @GetMapping("/food")
    public List<Rating> foodWithRatings(@RequestParam double rate) throws GlobalException {
        return ratingService.foodWithRatings(rate);
    }

}
