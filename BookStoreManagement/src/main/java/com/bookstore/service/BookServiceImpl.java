package com.bookstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.exception.BookNotFoundException;
import com.bookstore.exception.DuplicateDataException;
import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;


@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository; // Dependency injection of BookRepository
    
    

    /**
     * Adds a new book to the repository. Checks for duplicate titles before saving.
     * Throws DuplicateDataException if a book with the same title exists.
     * 
     * @param book The Book object to be added.
     * @return The saved Book object.
     */
    @Override
    public Book addBook(Book book) {
        // Check if a book with the same title already exists
        Optional<Book> existingBook = bookRepository.findByTitle(book.getTitle());
        if (existingBook.isPresent()) {
            // If found, throw DuplicateDataException
            throw new DuplicateDataException("Book with the same title already exists.");
        }
        // Save the new book to the repository
        return bookRepository.save(book);
    }
    
    
    
    

    /**
     * Updates an existing book identified by bookId. Checks for duplicate titles before updating.
     * Throws BookNotFoundException if the book is not found and DuplicateDataException 
     * if a book with the new title already exists.
     * 
     * @param bookId The ID of the book to be updated.
     * @param updatedBook The Book object containing the updated details.
     * @return The updated Book object.
     */
    @Override
    public Book updateBook(Long bookId, Book updatedBook) {
        // Retrieve the book by ID, or throw an exception if not found
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));
        
        // Check if the title is being updated and if the new title already exists
        if (updatedBook.getTitle() != null && !updatedBook.getTitle().equals(book.getTitle())) {
            Optional<Book> existingBook = bookRepository.findByTitle(updatedBook.getTitle());
            if (existingBook.isPresent()) {
                // If a book with the new title exists, throw DuplicateDataException
                throw new DuplicateDataException("Book with the same title already exists.");
            }
            // Update the book title
            book.setTitle(updatedBook.getTitle());
        }
        // Update other book fields if they are not null
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
        // Save the updated book to the repository
        return bookRepository.save(book);
    }

    
    
    
    /**
     * Retrieves a book by its ID. Throws BookNotFoundException if no book is found 
     * with the specified ID.
     * 
     * @param bookId The ID of the book to be retrieved.
     * @return The Book object with the specified ID.
     */
    @Override
    public Book getBookById(Long bookId) {
        // Retrieve the book by ID, or throw an exception if not found
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));
    }

    
    
    
    
    /**
     * Retrieves a book by its title. Throws BookNotFoundException if no book is found 
     * with the specified title.
     * 
     * @param title The title of the book to be retrieved.
     * @return The Book object with the specified title.
     */
    @Override
    public Book getBookByTitle(String title) {
        // Retrieve the book by title, or throw an exception if not found
        return bookRepository.findByTitle(title)
                .orElseThrow(() -> new BookNotFoundException("Book not found with title: " + title));
    }

    
    
    
    
    /**
     * Retrieves all books from the repository. Throws BookNotFoundException if no books 
     * are found, indicating that the list is empty.
     * 
     * @return A list of all Book objects.
     */
    @Override
    public List<Book> getAllBooks() {
        // Retrieve all books from the repository
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            // If no books are found, throw BookNotFoundException
            throw new BookNotFoundException("Book list is empty");
        }
        // Return the list of books
        return books;
    }

    
    
    
    
    /**
     * Deletes a book identified by bookId from the repository. Throws BookNotFoundException 
     * if the book with the specified ID does not exist.
     * 
     * @param bookId The ID of the book to be deleted.
     */
    @Override
    public void deleteBook(Long bookId) {
        // Check if the book exists before attempting to delete
        if (!bookRepository.existsById(bookId)) {
            // If not found, throw BookNotFoundException
            throw new BookNotFoundException("Book not found with id: " + bookId);
        }
        // Delete the book from the repository
        bookRepository.deleteById(bookId);
    }
}
