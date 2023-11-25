package com.backend.foodproject.reposiory;

import com.backend.foodproject.entity.WebOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<WebOrder, Long> {
}
