package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.example.demo.dto.BookRequest;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

@Service
@RequiredArgsConstructor
public class BookService { 
    private final BookRepository bookRepository;

    @Cacheable(key= "#id", value = "book")
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    // value is the returned type, key is the parameter used to filter the cache
    @Cacheable(key = "#author", value = "booksByAuthor")
    public List<Book> getBooksByAuthor(String author) {
        System.out.println("Fetching books from DB for author: " + author);
        return bookRepository.findByAuthor(author);
    }

    @Caching( 
        evict = {
            @CacheEvict(value = "booksByAuthor", allEntries = true),
            @CacheEvict(value = "book", key = "#book.id"),
            @CacheEvict(value = "allBooks", allEntries = true)
    })
    public Book createBook(BookRequest book) {
        Book newBook = new Book();
        newBook.setTitle(book.getTitle());
        newBook.setAuthor(book.getAuthor());
        return bookRepository.save(newBook);
    }

    @Cacheable(value = "allBooks")
    public List<Book> getAllBooks() { 
        return bookRepository.findAll();
    }

}