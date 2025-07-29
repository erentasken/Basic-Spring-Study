package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.Book;
import com.example.demo.security.JwtFilter;
import com.example.demo.service.BookService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private JwtFilter jwtFilter;

    @Test
    void getBook_ShouldReturnBook() throws Exception {
        // Mock the service to return a book
        Book book = new Book(1L, "Effective Java", "Joshua");
        when(bookService.getBookById(1L)).thenReturn(book);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Effective Java"))
                .andExpect(jsonPath("$.author").value("Joshua"));
    }

    @Test
    void getBook_ShouldReturnNotFound_WhenBookDoesNotExist() throws Exception {
        // Mock the service to return null for a non-existing book
        when(bookService.getBookById(999L)).thenReturn(null);
        // Perform the GET request and verify the response
        mockMvc.perform(get("/api/books/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllBooks_ShouldReturnListOfBooks() throws Exception {
        // Mock the service to return a list of books
        Book book1 = new Book(1L, "Effective Java", "Joshua");
        Book book2 = new Book(2L, "Clean Code", "Robert C. Martin");
        when(bookService.getAllBooks()).thenReturn(List.of(book1, book2));

        // Perform the GET request and verify the response
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Effective Java"))
                .andExpect(jsonPath("$[1].title").value("Clean Code"));
    }

    @Test
    void getBooksByAuthor_ShouldReturnListOfBooks() throws Exception {
        // Mock the service to return a list of books by author
        Book book1 = new Book(1L, "Effective Java", "Joshua");
        Book book2 = new Book(2L, "Java Concurrency in Practice", "Joshua");
        when(bookService.getBooksByAuthor("Joshua")).thenReturn(List.of(book1, book2));

        // Perform the GET request and verify the response
        mockMvc.perform(get("/api/books/author/Joshua"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Effective Java"))
                .andExpect(jsonPath("$[1].title").value("Java Concurrency in Practice"));
    }

    @Test
    void createBook_ShouldReturnCreatedBook() throws Exception {
        // Create a book object to be returned
        Book book = new Book(1L, "Effective Java", "Joshua");
        when(bookService.createBook(book)).thenReturn(book);

        // Perform the POST request and verify the response
        mockMvc.perform(post("/api/books")
                .contentType("application/json")
                .content("{\"id\":1,\"title\":\"Effective Java\",\"author\":\"Joshua\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Effective Java"))
                .andExpect(jsonPath("$.author").value("Joshua"));
    }
}
