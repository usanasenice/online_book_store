package com.bookstore.usanase.repository;

import com.bookstore.usanase.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserId(Long userId);
}