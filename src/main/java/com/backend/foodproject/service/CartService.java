package com.backend.foodproject.service;

import com.backend.foodproject.entity.Cart;
import com.backend.foodproject.entity.Food;
import com.backend.foodproject.entity.User;
import com.backend.foodproject.error.GlobalException;
import com.backend.foodproject.reposiory.CartRepository;
import com.backend.foodproject.reposiory.FoodRepository;
import com.backend.foodproject.reposiory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final FoodRepository foodRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public Cart addCart(Cart cart, User user, Long id,String email) throws GlobalException {
        User existUser = userRepository.findByEmail(user.getEmail());
        Food food = foodRepository.findById(id).get();
        Cart createCart = Cart.builder()
                .quantity(cart.getQuantity())
                .totalPrice(food.getPrice() * cart.getQuantity())
                .user(existUser)
                .foods(List.of(food))
                .build();
        return cartRepository.save(createCart);
    }

    public List<Cart> getCartList(User user) throws GlobalException {
        userRepository.findByEmail(user.getEmail());
        List<Cart> cartList = cartRepository.findAll();
        if (cartList.isEmpty()) {
            throw new GlobalException("carts not available");
        }
        return cartList;
    }

    public Cart getCart(User user, Long id) throws GlobalException {
        userRepository.findByEmail(user.getEmail());
        Optional<Cart> cart = cartRepository.findById(id);
        if (cart.isEmpty()) {
            throw new GlobalException("carts not available");
        }
        return cart.get();
    }

    public Cart updateQuantity(Cart cart, User user, Long id) {
        userRepository.findByEmail(user.getEmail());
        var food = foodRepository.findById(id).get();
        Cart updateCart = cartRepository.findById(id).get();
        updateCart.setQuantity(cart.getQuantity());
        updateCart.setTotalPrice(updateCart.getQuantity() * food.getPrice());
        return cartRepository.save(updateCart);

    }

    public String removeCart(User user, Long id) {
        userRepository.findByEmail(user.getEmail());
        cartRepository.findById(id).get();
        cartRepository.deleteById(id);
        return "food removed from cart";
    }
}
