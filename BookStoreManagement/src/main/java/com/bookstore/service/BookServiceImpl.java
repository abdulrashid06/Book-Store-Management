package com.bookstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.exception.BookNotFoundException;
import com.bookstore.exception.DuplicateDataException;
import com.bookstore.exception.InvalidDataException;
import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;


@Service
public class BookServiceImpl implements BookService {

	@Autowired
    private BookRepository bookRepository;

    @Override
    public Book addBook(Book book){
        Optional<Book> existingBook = bookRepository.findByTitle(book.getTitle());
        if (existingBook.isPresent()) {
            throw new DuplicateDataException("Book with the same title already exists.");
        }
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Long bookId, Book updatedBook) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));
        
        if (updatedBook.getTitle() != null && !updatedBook.getTitle().equals(book.getTitle())) {
            Optional<Book> existingBook = bookRepository.findByTitle(updatedBook.getTitle());
            if (existingBook.isPresent()) {
                throw new DuplicateDataException("Book with the same title already exists.");
            }
            book.setTitle(updatedBook.getTitle());
        }
        if (updatedBook.getAuthor() != null) {
            book.setAuthor(updatedBook.getAuthor());
        }
        if (updatedBook.getDescription() != null) {
            book.setDescription(updatedBook.getDescription());
        }
        if (updatedBook.getPrice() != null) {
            book.setPrice(updatedBook.getPrice());
        }
        if (updatedBook.getStock() != null) {
            book.setStock(updatedBook.getStock());
        }
        return bookRepository.save(book);
    }

    @Override
    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));
    }

    
    @Override
    public Book getBookByTitle(String title) {
        return bookRepository.findByTitle(title)
                .orElseThrow(() -> new BookNotFoundException("Book not found with title: " + title));
    }

    
    @Override
    public List<Book> getAllBooks() {
    	List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            // Return an empty list instead of throwing an exception
        	throw new BookNotFoundException("Book list is empty");
//            return books; 
        }
        return books;
    }
    

    @Override
    public void deleteBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException("Book not found with id: " + bookId);
        }
        bookRepository.deleteById(bookId);
    }

}
