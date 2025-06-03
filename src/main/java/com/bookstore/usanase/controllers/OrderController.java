package com.bookstore.usanase.controllers;
import com.bookstore.usanase.model.Order;
import com.bookstore.usanase.model.User;
import com.bookstore.usanase.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping("/checkout")
    public Order checkout(Authentication authentication) {
        Long userId = ((User) authentication.getPrincipal()).getId();
        return orderService.createOrder(userId);
    }
}
