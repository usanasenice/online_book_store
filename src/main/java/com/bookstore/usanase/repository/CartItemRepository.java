package com.bookstore.usanase.repository;
import java.util.*;
import com.bookstore.usanase.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // Optional: Add custom query methods if needed
    List<CartItem> findByCartId(Long cartId);
}