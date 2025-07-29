package com.example.demo.service;

import java.util.List;
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
    
    @Test
    void getBooksByAuthor_ShouldReturnBooks_WhenBooksExist() {
        // Arrange
        Book book1 = new Book(1L, "Book One", "Author A");
        Book book2 = new Book(2L, "Book Two", "Author A");
        when(bookRepository.findByAuthor("Author A")).thenReturn(List.of(book1, book2));

        // Act
        List<Book> books = bookService.getBooksByAuthor("Author A");

        // Assert
        assertEquals(2, books.size());
        assertEquals("Book One", books.get(0).getTitle());
        assertEquals("Book Two", books.get(1).getTitle());
        verify(bookRepository, times(1)).findByAuthor("Author A");
    }
}
