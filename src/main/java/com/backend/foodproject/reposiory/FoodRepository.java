package com.backend.foodproject.reposiory;

import com.backend.foodproject.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food,Long> {

    List<Food> findByPriceBetween(double minprice, double maxprice);
}
