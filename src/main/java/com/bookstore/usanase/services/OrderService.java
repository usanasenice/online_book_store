package com.bookstore.usanase.services;
import com.bookstore.usanase.model.*;
import com.bookstore.usanase.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentService paymentService;
    public Order createOrder(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        double totalAmount = 0;
        for (CartItem item : cart.getItems()) {
            totalAmount += item.getBook().getPrice() * item.getQuantity();
        }
        Payment payment = paymentService.processPayment(null, totalAmount);
        Order order = new Order();
        User user = new User();
        user.setId(userId);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(totalAmount);
        order.setPayment(payment);
        // For simplicity, create one order per book item in the cart
        for (CartItem item : cart.getItems()) {
            Order itemOrder = new Order();
            itemOrder.setUser(user);
            itemOrder.setBook(item.getBook());
            itemOrder.setQuantity(item.getQuantity());
            itemOrder.setTotalPrice(item.getBook().getPrice() * item.getQuantity());
            itemOrder.setOrderDate(LocalDateTime.now());
            itemOrder.setPayment(payment);
            orderRepository.save(itemOrder);
        }
        // Clear the cart after checkout
        cart.getItems().clear();
        cartRepository.save(cart);

        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}