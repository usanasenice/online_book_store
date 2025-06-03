package com.bookstore.usanase.services;
import com.bookstore.usanase.model.*;
import com.bookstore.usanase.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private BookRepository bookRepository;

    public Cart addToCart(Long userId, Long bookId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            cart = new Cart();
            User user = new User();
            user.setId(userId);
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }

        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setBook(book.get());
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
        return cartRepository.save(cart);
    }

    public Cart updateCartItem(Long cartItemId, int quantity) {
        Optional<CartItem> item = cartItemRepository.findById(cartItemId);
        if (item.isPresent()) {
            item.get().setQuantity(quantity);
            cartItemRepository.save(item.get());
        }
        return item.get().getCart();
    }

    public void removeFromCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}