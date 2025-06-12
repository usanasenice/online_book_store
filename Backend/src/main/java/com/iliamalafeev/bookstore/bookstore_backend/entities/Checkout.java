package com.iliamalafeev.bookstore.bookstore_backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "checkout")
public class Checkout {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getCheckoutHolder() {
        return checkoutHolder;
    }

    public void setCheckoutHolder(Person checkoutHolder) {
        this.checkoutHolder = checkoutHolder;
    }

    public Book getCheckedOutBook() {
        return checkedOutBook;
    }

    public void setCheckedOutBook(Book checkedOutBook) {
        this.checkedOutBook = checkedOutBook;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "person_email", referencedColumnName = "email")
    @JsonIgnore
    private Person checkoutHolder;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    @JsonIgnoreProperties("checkouts, historyRecords, reviews")
    private Book checkedOutBook;

    @Column(name = "checkout_date")
    private LocalDate checkoutDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    public Checkout(Person checkoutHolder, Book checkedOutBook, LocalDate checkoutDate, LocalDate returnDate) {
        this.checkoutHolder = checkoutHolder;
        this.checkedOutBook = checkedOutBook;
        this.checkoutDate = checkoutDate;
        this.returnDate = returnDate;
    }
}
