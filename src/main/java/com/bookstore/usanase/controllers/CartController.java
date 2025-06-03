package com.bookstore.usanase.controllers;

import com.bookstore.usanase.model.Cart;
import com.bookstore.usanase.model.User;
import com.bookstore.usanase.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public Cart addToCart(@RequestParam Long bookId, @RequestParam int quantity, Authentication authentication) {
        Long userId = ((User) authentication.getPrincipal()).getId();
        return cartService.addToCart(userId, bookId, quantity);
    }

    @PutMapping("/update")
    public Cart updateCartItem(@RequestParam Long cartItemId, @RequestParam int quantity) {
        return cartService.updateCartItem(cartItemId, quantity);
    }

    @DeleteMapping("/remove")
    public void removeFromCart(@RequestParam Long cartItemId) {
        cartService.removeFromCart(cartItemId);
    }
}