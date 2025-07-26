package com.example.demo.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.assertj.core.api.Assertions.assertThat;

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
}
