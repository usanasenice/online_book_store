package com.bookstore.usanase.repository;



import com.bookstore.usanase.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
