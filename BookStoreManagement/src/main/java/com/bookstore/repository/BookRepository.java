package com.bookstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    // Method to find a Book by its title
    Optional<Book> findByTitle(String title);
}
