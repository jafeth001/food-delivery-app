package com.backend.foodproject.service;

import com.backend.foodproject.entity.Rating;
import com.backend.foodproject.entity.Seller;
import com.backend.foodproject.error.GlobalException;
import com.backend.foodproject.reposiory.FoodRepository;
import com.backend.foodproject.reposiory.RatingRepository;
import com.backend.foodproject.reposiory.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final SellerRepository sellerRepository;
    private final FoodRepository foodRepository;

    public String addRating(Seller seller, Rating rating, Long id) {
        sellerRepository.findByEmail(seller.getEmail());
        var food = foodRepository.findById(id).get();
        var addRatings = Rating.builder()
                .ratings(rating.getRatings())
                .food(food)
                .build();
        ratingRepository.save(addRatings);
        return "rating successfully added";
    }

    public List<Rating> foodWithRatings(double rate) throws GlobalException {
        List<Rating> ratings = ratingRepository.findAllByRatings(rate);
        if (!ratings.isEmpty()) {
            return ratings;
        }
        throw new GlobalException("products with rating " + rate + " not found");
    }
}
