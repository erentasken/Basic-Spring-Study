package com.example.demo.service;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void getBookById_ShouldReturnBook_WhenBookExists() { 
        // Arrange
        Book book = new Book(1L, "Test Book", "Test Author");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        
        // Act
        Book foundBook = bookService.getBookById(1L);

        // Assert 
        assertEquals("Test Book", foundBook.getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void getBookById_ShouldThrowException_WhenBookDoesNotExist() { 
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            bookService.getBookById(1L);
        });

        // Verify
        verify(bookRepository, times(1)).findById(1L);
    }
    
}
