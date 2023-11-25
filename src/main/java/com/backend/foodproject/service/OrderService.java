package com.backend.foodproject.service;

import com.backend.foodproject.entity.*;
import com.backend.foodproject.error.GlobalException;
import com.backend.foodproject.reposiory.*;
import com.backend.foodproject.utils.EmailUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final SellerRepository sellerRepository;
    private final EmailUtil emailUtil;

    public String requestWebOrder(Long id, String email, WebOrder webOrder, User user) throws GlobalException {
        var exixtUser = userRepository.findByEmail(user.getEmail());
        var cart = cartRepository.findById(id).get();
        var address = webOrder.getAddress();
        Address createdAddress = Address.builder()
                .city(address.getCity())
                .road(address.getRoad())
                .phone(address.getPhone())
                .street(address.getStreet())
                .build();
        var savedAddress = addressRepository.save(createdAddress);
        WebOrder saveOrder = WebOrder.builder()
                .cart(cart)
                .address(savedAddress)
                .orderStatus(OrderStatus.REQUESTED)
                .orderDate(LocalDateTime.now())
                .build();
        orderRepository.save(saveOrder);
        return "order requested successfully...proceed to confirm order";
    }

    public List<WebOrder> getorders(User user) throws GlobalException {
        userRepository.findByEmail(user.getEmail());
        List<WebOrder> webOrders = orderRepository.findAll();
        if (webOrders.isEmpty()) {
            throw new GlobalException("orders not found");
        }
        return webOrders;
    }

    public WebOrder getOrder(Long id, User user) throws GlobalException {
        userRepository.findByEmail(user.getEmail());
        Optional<WebOrder> order = orderRepository.findById(id);
        if (order.isPresent()) {
            return order.get();
        }
        throw new GlobalException("order with id " + id + " not found");
    }

    public WebOrder confirmOrder(Long id, User user) throws GlobalException {
        userRepository.findByEmail(user.getEmail());
        var existOrder = orderRepository.findById(id).get();
        if (existOrder == null) {
            throw new GlobalException("order with id " + id + "not found");
        }
        existOrder.setOrderStatus(OrderStatus.CONFIRMED);
        return orderRepository.save(existOrder);
    }

    public WebOrder deliveredOrder(Long id, User user) throws GlobalException {
        userRepository.findByEmail(user.getEmail());
        var existOrder = orderRepository.findById(id).get();
        if (existOrder == null) {
            throw new GlobalException("order with id " + id + "not found");
        }
        existOrder.setOrderStatus(OrderStatus.DELIVERED);
        return orderRepository.save(existOrder);
    }

    public String deleteOrder(User user, Long id) throws GlobalException {
        userRepository.findByEmail(user.getEmail());
        var existOrder = orderRepository.findById(id).get();
        if (existOrder == null) {
            throw new GlobalException("no order to delete with id" + id);
        }
        orderRepository.delete(existOrder);
        return "order successfully deleted ";
    }
}
