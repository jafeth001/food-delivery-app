package com.backend.foodproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String token;
    private boolean revoked;
    private boolean expired;
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    @ManyToOne
    private User user;
    @ManyToOne
    private Seller seller;
}
