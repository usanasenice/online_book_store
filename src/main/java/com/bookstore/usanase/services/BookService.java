package com.bookstore.usanase.services;

import com.bookstore.usanase.model.Book;
import com.bookstore.usanase.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
@Service
public abstract class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private RestTemplate restTemplate;
    public List<Book> getAllBooks() {
        // Mock external API call (replace with real API like Google Books)
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            // Seed with mock data
            Book book1 = new Book();
            book1.setTitle("Sample Book 1");
            book1.setAuthor("Author 1");
            book1.setPrice(29.99);
            book1.setIsbn("1234567890");
            bookRepository.save(book1);
            books.add(book1);
        }
        return books;
    }

    public abstract Book getBookById(Long id);

    public abstract Book saveBook(Book book);

    public abstract void deleteBook(Long id);
}