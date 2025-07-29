package com.example.demo.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import com.example.demo.model.Book;


@DataJpaTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    void saveBook_ShouldPersist(){ 
        // Arrange
        Book book = new Book(null, "Test Title", "Test Author");

        Book savedBook = bookRepository.save(book);

        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getId()).isGreaterThan(0);
        assertThat(savedBook.getAuthor()).isEqualTo("Test Author");
        assertThat(savedBook.getTitle()).isEqualTo("Test Title");

    }

    @Test
    void findByAuthor_ShouldReturnBooks() { 
        bookRepository.save(new Book(null, "Book 1", "Author A"));
        bookRepository.save(new Book(null, "Book 2", "Author B"));

        List<Book> books = bookRepository.findByAuthor("Author A");

        assertThat(books).hasSize(1);
        assertThat(books.get(0).getTitle()).isEqualTo("Book 1");
    }

    @Test
    void findByAuthor_ShouldReturnEmptyList_WhenNoBooksFound() {
        List<Book> books = bookRepository.findByAuthor("Nonexistent Author");
        assertThat(books).isEmpty();
    }

    @Test
    void findById_ShouldReturnBook_WhenBookExists() { 
        Book book = bookRepository.save(new Book(null, "Test Title", "Test Author"));
        Book foundBook = bookRepository.findById(book.getId()).orElse(null);
        assertThat(foundBook).isNotNull();
        assertThat(foundBook.getTitle()).isEqualTo("Test Title");
        assertThat(foundBook.getAuthor()).isEqualTo("Test Author");
    }

    @Test
    void createBook_ShouldReturnCreatedBook() { 
        Book createdBook = bookRepository.save(new Book(5L, "New Book", "New Author"));

        bookRepository.findById(createdBook.getId()).ifPresentOrElse(
            book -> {
                assertThat(book.getTitle()).isEqualTo("New Book");
                assertThat(book.getAuthor()).isEqualTo("New Author");
            },
            () -> fail("Book not found")
        );
    }
}
