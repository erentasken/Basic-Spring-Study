package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;


import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) { 
        return bookService.getBookById(id);
    }

    @GetMapping("/author/{author}")
    public List<Book> getBooksByAuthor(@PathVariable String author) { 
        return bookService.getBooksByAuthor(author);
    }

    @GetMapping
    public List<Book> getAllBooks() { 
        return bookService.getAllBooks();
    }

    @PostMapping
    public Book createBook(@RequestBody Book book) { 
        return bookService.createBook(book);
    }


}
