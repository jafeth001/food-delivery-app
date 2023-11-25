package com.backend.foodproject.service;

import com.backend.foodproject.entity.OrderStatus;
import com.backend.foodproject.entity.Seller;
import com.backend.foodproject.entity.User;
import com.backend.foodproject.entity.WebOrder;
import com.backend.foodproject.error.GlobalException;
import com.backend.foodproject.reposiory.OrderRepository;
import com.backend.foodproject.reposiory.SellerRepository;
import com.backend.foodproject.reposiory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public List<User> userList(Seller seller) throws GlobalException {
        sellerRepository.findByEmail(seller.getEmail());
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new GlobalException("users not found");
        }
        return users;
    }

    public User userById(Seller seller, Long id) throws GlobalException {
        sellerRepository.findByEmail(seller.getEmail());
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new GlobalException("user with id " + id + " not found");
        }
        return user.get();
    }

    public String deactivateUser(Seller seller, User user, Long id) {
        sellerRepository.findByEmail(seller.getEmail());
        User existuser = userRepository.findById(id).get();
        existuser.setActive(false);
        userRepository.save(existuser);
        return "user with id " + id + " deactivated successfully";
    }

    public String activateUser(Seller seller, User user, Long id) {
        sellerRepository.findByEmail(seller.getEmail());
        User existuser = userRepository.findById(id).get();
        existuser.setActive(true);
        userRepository.save(existuser);
        return "user with id " + id + " activated successfully";

    }

    public List<WebOrder> getorders(Seller seller) throws GlobalException {
        userRepository.findByEmail(seller.getEmail());
        List<WebOrder> webOrders = orderRepository.findAll();
        if (webOrders.isEmpty()) {
            throw new GlobalException("orders not found");
        }
        return webOrders;
    }

    public WebOrder getOrder(Long id, Seller seller) throws GlobalException {
        userRepository.findByEmail(seller.getEmail());
        Optional<WebOrder> order = orderRepository.findById(id);
        if (order.isPresent()) {
            return order.get();
        }
        throw new GlobalException("order with id " + id + " not found");
    }
}
