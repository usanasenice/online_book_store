package com.iliamalafeev.bookstore.bookstore_backend.controllers;

import com.iliamalafeev.bookstore.bookstore_backend.dto.BookDTO;
import com.iliamalafeev.bookstore.bookstore_backend.dto.ReviewDTO;
import com.iliamalafeev.bookstore.bookstore_backend.entities.Book;
import com.iliamalafeev.bookstore.bookstore_backend.services.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @Test
    void findAll_ShouldReturnPageOfBooks() {
        // Arrange
        Page<BookDTO> mockPage = new PageImpl<>(List.of(new BookDTO()));
        when(bookService.findAll(anyInt(), anyInt())).thenReturn(mockPage);

        // Act
        ResponseEntity<Page<BookDTO>> response = bookController.findAll(0, 9);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(bookService).findAll(0, 9);
    }

    @Test
    void findAll_ShouldReturnEmptyPage() {
        // Arrange
        Page<BookDTO> emptyPage = Page.empty();
        when(bookService.findAll(anyInt(), anyInt())).thenReturn(emptyPage);

        // Act
        ResponseEntity<Page<BookDTO>> response = bookController.findAll(0, 9);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(bookService).findAll(0, 9);
    }

    @Test
    void findById_ShouldReturnBook() {
        // Arrange
        Long bookId = 1L;
        BookDTO bookDTO = new BookDTO();
        when(bookService.findById(bookId)).thenReturn(bookDTO);

        // Act
        ResponseEntity<BookDTO> response = bookController.findById(bookId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(bookService).findById(bookId);
    }

    @Test
    void findById_ShouldReturnNotFound() {
        // Arrange
        Long bookId = 1L;
        when(bookService.findById(bookId)).thenReturn(null);

        // Act
        ResponseEntity<BookDTO> response = bookController.findById(bookId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(bookService).findById(bookId);
    }
}
