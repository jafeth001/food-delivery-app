package com.backend.foodproject.service;

import com.backend.foodproject.entity.Category;
import com.backend.foodproject.entity.Food;
import com.backend.foodproject.entity.Rating;
import com.backend.foodproject.entity.Seller;
import com.backend.foodproject.error.GlobalException;
import com.backend.foodproject.reposiory.CategoryRepository;
import com.backend.foodproject.reposiory.FoodRepository;
import com.backend.foodproject.reposiory.RatingRepository;
import com.backend.foodproject.reposiory.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;
    private final RatingRepository ratingRepository;

    public String addFood(Food food, Seller seller, Long id) {
        Seller newSeller = sellerRepository.findByEmail(seller.getEmail());
        Category availableCat = categoryRepository.findById(id).get();
        Food addFood = Food.builder()
                .category(availableCat)
                .name(food.getName())
                .description(food.getDescription())
                .imageUrl(food.getImageUrl())
                .price(food.getPrice())
                .seller(newSeller)
                .build();
        foodRepository.save(addFood);
        return "food successfully saved";
    }

    public List<Food> getFoodLists() throws GlobalException {
        List<Food> foods = foodRepository.findAll();
        if (!foods.isEmpty()) {
            return foods;
        }
        throw new GlobalException("foods not available");
    }

    public Food getFood(Long id) throws GlobalException {
        Optional<Food> food = foodRepository.findById(id);
        if (food.isPresent()) {
            return food.get();
        }
        throw new GlobalException("food with id " + id + " not found");
    }

    public List<Food> getFoodPrice(double minprice, double maxprice) throws GlobalException {
        List<Food> foods = foodRepository.findByPriceBetween(minprice,maxprice);
        if (!foods.isEmpty()) {
            return foods;
        }
        throw new GlobalException("foods not available for that price range");
    }
    public String updateFood(Long id, Food food, Seller seller) throws GlobalException {
         sellerRepository.findByEmail(seller.getEmail());
        Food newFood = foodRepository.findById(id).get();
        if (Objects.nonNull(food.getName()) && !"".equalsIgnoreCase(food.getName())) {
            newFood.setName(food.getName());
        }
        if (Objects.nonNull(food.getDescription()) && !"".equalsIgnoreCase(food.getDescription())) {
            newFood.setDescription(food.getDescription());
        }
        if (Objects.nonNull(food.getPrice())) {
            newFood.setPrice(food.getPrice());
        }
        if (Objects.nonNull(food.getImageUrl()) && !"".equalsIgnoreCase(food.getImageUrl())) {
            newFood.setImageUrl(food.getImageUrl());
        }
        foodRepository.save(newFood);
        return "food updated successfully";
    }

    public String deleteFood(Long id, Seller seller) throws GlobalException {
        sellerRepository.findByEmail(seller.getEmail());
        Food food = foodRepository.findById(id).get();
        foodRepository.deleteById(id);
        return "food deleted sucessfully";
    }

}