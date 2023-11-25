package com.backend.foodproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany
    private List<Food> foods;
    @JsonIgnore
    @OneToOne
    private WebOrder webOrder;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private int quantity;
    private Double totalPrice;
}
