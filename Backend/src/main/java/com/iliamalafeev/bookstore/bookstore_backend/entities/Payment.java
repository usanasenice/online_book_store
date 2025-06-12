package com.iliamalafeev.bookstore.bookstore_backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getPaymentHolder() {
        return paymentHolder;
    }

    public void setPaymentHolder(Person paymentHolder) {
        this.paymentHolder = paymentHolder;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @OneToOne
    @JoinColumn(name = "person_email", referencedColumnName = "email")
    @JsonIgnoreProperties("payment")
    private Person paymentHolder;

    @Column(name = "amount")
    private Double amount;

    public Payment(Person paymentHolder, Double amount) {
        this.paymentHolder = paymentHolder;
        this.amount = amount;
    }
}
