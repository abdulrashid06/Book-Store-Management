package com.bookstore.service;

import java.util.List;

import com.bookstore.model.Book;

public interface BookService {
	
    public Book addBook(Book book);
    
    public Book updateBook(Long bookId, Book updatedBook);
    
    public Book getBookById(Long bookId);
    
    public Book getBookByTitle(String title);
    
    public List<Book> getAllBooks();
    
    public void deleteBook(Long bookId);

}
