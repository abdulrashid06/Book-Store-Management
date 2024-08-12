package com.bookstore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.model.Book;
import com.bookstore.service.BookServiceImpl;


@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookServiceImpl bookService;
    
    
    

    /**
     * Handles the creation of a new book. This endpoint accepts the book details 
     * in the request body and saves it to the database. If the book is successfully 
     * created, it returns a response with the created book and an HTTP status of 201 (Created).
     *
     * @param book The book object to be created.
     * @return ResponseEntity containing the created Book and HTTP status 201.
     */
    @PostMapping("/addBooks")
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
    	System.out.println(book);
        Book createdBook = bookService.addBook(book);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }
    
    
    
    

    /**
     * Retrieves a specific book by its ID. This endpoint returns the book 
     * if found, along with an HTTP status of 200 (OK).
     *
     * @param id The ID of the book to retrieve.
     * @return ResponseEntity containing the requested Book and HTTP status 200.
     */
    @GetMapping("/create/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    
    
    
    
    /**
     * Retrieves a list of all books in the system. This endpoint returns a 
     * list of books, along with an HTTP status of 200 (OK).
     *
     * @return ResponseEntity containing a list of Books and HTTP status 200.
     */
    @GetMapping("/get_book_list")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    
    
    
    
    /**
     * Updates an existing book by its ID. This endpoint accepts the book details 
     * in the request body and updates the corresponding book in the database. 
     * The response contains the updated book and an HTTP status of 202 (Accepted).
     *
     * @param id The ID of the book to update.
     * @param bookDetails The details of the book to be updated.
     * @return ResponseEntity containing the updated Book and HTTP status 202.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        Book updatedBook = bookService.updateBook(id, bookDetails);
        return new ResponseEntity<>(updatedBook, HttpStatus.ACCEPTED);
    }

    
    
    
    
    /**
     * Deletes a specific book by its ID. This endpoint deletes the book if it exists 
     * and returns an HTTP status of 204 (No Content) to indicate that the deletion was successful.
     *
     * @param id The ID of the book to delete.
     * @return ResponseEntity with HTTP status 204 indicating that the book was successfully deleted.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    
}
