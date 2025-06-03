package com.bookstore.usanase.repository;
import com.bookstore.usanase.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}