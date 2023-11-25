package com.backend.foodproject.reposiory;

import com.backend.foodproject.entity.Food;
import com.backend.foodproject.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Long> {
    List<Rating> findAllByRatings(double rate);
}
