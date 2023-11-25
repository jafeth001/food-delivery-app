package com.backend.foodproject.reposiory;

import com.backend.foodproject.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller,Long> {
    public Seller findByEmail(String email);
}
