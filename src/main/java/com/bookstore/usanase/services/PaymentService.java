package com.bookstore.usanase.services;

import com.bookstore.usanase.model.Payment;
import com.bookstore.usanase.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment processPayment(Long orderId, double amount) {
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setStatus("PAID"); // Mock payment processing
        payment.setTransactionId("TXN_" + System.currentTimeMillis());
        return paymentRepository.save(payment);
    }
}

