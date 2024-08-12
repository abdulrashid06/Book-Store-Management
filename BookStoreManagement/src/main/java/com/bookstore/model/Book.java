package com.bookstore.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is mandatory")
    @Size(max = 100, message = "Title should not exceed 100 characters")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Author is mandatory")
    @Size(max = 50, message = "Author should not exceed 50 characters")
    @Column(nullable = false)
    private String author;

    @NotBlank(message = "Description is mandatory")
    @Size(max = 500, message = "Description should not exceed 500 characters")
    @Column(nullable = false)
    private String description;

    @NotNull(message = "Price is mandatory")
    @Min(value = 0, message = "Price cannot be negative")
    @Column(nullable = false)
    private Double price;

    @NotNull(message = "Stock is mandatory")
    @Min(value = 0, message = "Stock cannot be negative")
    @Column(nullable = false)
    private Integer stock;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Getters and Setters
}
