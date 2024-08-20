package com.bookstore.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.bookstore.config.JwtUtils;
import com.bookstore.exception.BookNotFoundException;
import com.bookstore.exception.OrderNotFoundException;
import com.bookstore.exception.UserNotFoundException;
import com.bookstore.model.Book;
import com.bookstore.model.Order;
import com.bookstore.model.Order.Status;
import com.bookstore.model.OrderResponseDTO;
import com.bookstore.model.User;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.OrderRepository;
import com.bookstore.repository.UserRepository;

@Service
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private OrderRepository orderRepository; // Dependency injection of OrderRepository

    @Autowired
    private UserRepository userRepository; // Dependency injection of UserRepository

    @Autowired
    private BookRepository bookRepository; // Dependency injection of BookRepository
    
    
    
    
    
    /**
     * Places a new order for a book. Validates user and book existence, checks stock, and creates an order.
     * Throws IllegalArgumentException if insufficient stock is available.
     * 
     * @param token The JWT token to decode the user's email.
     * @param bookId The ID of the book to be ordered.
     * @param quantity The quantity of the book to be ordered.
     * @return The created Order object.
     */
    @Override
    public Order placeOrder(String token, Long bookId, int quantity) {
        // Decode the token to get the user's email
        String email = JwtUtils.decodeJwt(token);
        // Find the user by email, or throw UserNotFoundException if not found
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        // Find the book by ID, or throw BookNotFoundException if not found
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        // Check if sufficient stock is available
        if (book.getStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock available");
        }

        // Create the order
        Order order = new Order();
        order.setUser(user);
        order.setBook(book);
        order.setQuantity(quantity);
        order.setTotalPrice(book.getPrice() * quantity);
        order.setStatus(Status.PLACED);

        // Add the order to the user's list of orders and save the user
        user.getOrders().add(order);
        userRepository.save(user);

        // Save the order to the repository
        return orderRepository.save(order);
    }

    
    
    
    
    /**
     * Updates an existing order. Currently not implemented.
     * 
     * @param orderId The ID of the order to be updated.
     * @param updatedOrder The Order object containing updated details.
     * @return The updated Order object.
     */
    @Override
    public Order updateOrder(Long orderId, Order updatedOrder) {
        return null; // Method implementation needed
    }

    
    
    
    
    /**
     * Retrieves an order by its ID and converts it to an OrderResponseDTO.
     * Throws OrderNotFoundException if the order is not found.
     * 
     * @param orderId The ID of the order to be retrieved.
     * @return An OrderResponseDTO containing order and related details.
     */
    @Override
    public OrderResponseDTO getOrderById(Long orderId) {
        // Find the order by ID, or throw OrderNotFoundException if not found
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

        // Create and populate the OrderResponseDTO
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setQuantity(order.getQuantity());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus().name());

        // Populate user DTO
        OrderResponseDTO.UserDTO userDTO = new OrderResponseDTO.UserDTO();
        userDTO.setId(order.getUser().getId());
        userDTO.setUsername(order.getUser().getUsername());
        userDTO.setEmail(order.getUser().getEmail());
        userDTO.setRole(order.getUser().getRole());
        dto.setUser(userDTO);

        // Populate book DTO
        OrderResponseDTO.BookDTO bookDTO = new OrderResponseDTO.BookDTO();
        bookDTO.setId(order.getBook().getId());
        bookDTO.setTitle(order.getBook().getTitle());
        bookDTO.setAuthor(order.getBook().getAuthor());
        bookDTO.setDescription(order.getBook().getDescription());
        bookDTO.setPrice(order.getBook().getPrice());
        bookDTO.setStock(order.getBook().getStock());
        dto.setBook(bookDTO);

        return dto;
    }

    
    
    
    
    /**
     * Retrieves all orders for the user if the role is USER, or all orders if the role is ADMIN.
     * Returns an empty list for other roles.
     * 
     * @param token The JWT token to decode the user's email.
     * @return A list of orders based on the user's role.
     */
    @Override
    public List<Order> getAllOrders(String token) {
        // Decode the token to get the user's email
        String email = JwtUtils.decodeJwt(token);
        // Find the user by email, or throw UserNotFoundException if not found
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Order not found"));

        // Return orders based on the user's role
        if (user.getRole().equals("USER")) {
            Long userId = user.getId(); // Assuming `User` entity has `getId()` method
            return orderRepository.findByUserId(userId);
        } else if (user.getRole().equals("ADMIN")) {
            return orderRepository.findAll();
        } else {
            // Handle other roles or return an empty list
            return Collections.emptyList();
        }
    }

    
    
    
    
    /**
     * Retrieves all orders with the status 'PLACED'.
     * 
     * @return A list of orders with the status 'PLACED'.
     */
    @Override
    public List<Order> getAllPlacedOrders() {
        Order.Status status = Order.Status.PLACED;
        return orderRepository.findByStatus(status);
    }
    
    
    
    
    
    /**
     * Retrieves all orders with the status 'SHIPPED'.
     * 
     * @return A list of orders with the status 'SHIPPED'.
     */
    @Override
    public List<Order> getAllShippedOrders() {
    	Order.Status status = Order.Status.SHIPPED;
    	return orderRepository.findByStatus(status);
    }

    
    
    
    
    /**
     * Retrieves all orders with the status 'DELIVERED'.
     * 
     * @return A list of orders with the status 'DELIVERED'.
     */
    @Override
    public List<Order> getAllDeliveredOrders() {
        Order.Status status = Order.Status.DELIVERED;
        return orderRepository.findByStatus(status);
    }

    
    
    
    
    /**
     * Deletes an order by its ID.
     * Throws OrderNotFoundException if the order is not found.
     * 
     * @param orderId The ID of the order to be deleted.
     */
    @Override
    public void deleteOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new OrderNotFoundException("Order not found with id: " + orderId);
        }
        // Delete the order by its ID
        orderRepository.deleteById(orderId);
    }

    
    
    
    
    /**
     * Updates the status of an order. Validates the current and new status to ensure proper transitions.
     * Throws IllegalStateException if the status transition is invalid.
     * 
     * @param orderId The ID of the order to be updated.
     * @param newStatus The new status to be set.
     * @return The updated Order object.
     */
    @Override
    public Order updateOrderStatus(Long orderId, Status newStatus) {
        // Find the order by ID, or throw OrderNotFoundException if not found
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        
        // Find the associated book by ID
        Book book = bookRepository.findById(order.getBook().getId())
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
        
        // Update book stock based on the order's quantity
        book.setStock(book.getStock() - order.getQuantity());
        bookRepository.save(book);

        // Current status of the order
        Status currentStatus = order.getStatus();

        // Validate status transition and update the status
        switch (newStatus) {
            case SHIPPED:
                if (currentStatus == Status.PLACED) {
                    order.setStatus(Status.SHIPPED);
                } else {
                    throw new IllegalStateException("Order can only be shipped if it is in the 'placed' status.");
                }
                break;

            case DELIVERED:
                if (currentStatus == Status.SHIPPED) {
                    order.setStatus(Status.DELIVERED);
                } else {
                    throw new IllegalStateException("Order can only be delivered if it is in the 'shipped' status.");
                }
                break;

            case CANCELLED:
                if (currentStatus == Status.PLACED || currentStatus == Status.SHIPPED) {
                    order.setStatus(Status.CANCELLED);
                } else {
                    throw new IllegalStateException("Order can only be canceled if it is in 'placed' or 'shipped' status.");
                }
                break;

            default:
                throw new IllegalArgumentException("Invalid status change requested.");
        }

        // Save and return the updated order
        return orderRepository.save(order);
    }

    
    
    
    
    /**
     * Cancels an order if its current status is 'PLACED' or 'SHIPPED'.
     * Throws IllegalStateException if the order cannot be canceled.
     * 
     * @param orderId The ID of the order to be canceled.
     * @return The updated Order object with 'CANCELLED' status.
     */
    @Override
    public Order cancelOrder(Long orderId) {
        // Find the order by ID, or throw OrderNotFoundException if not found
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        // Check if the order's status allows it to be canceled
        if (order.getStatus() == Status.PLACED || order.getStatus() == Status.SHIPPED) {
            order.setStatus(Status.CANCELLED);
            return orderRepository.save(order);
        } else {
            throw new IllegalStateException("Order can only be canceled if it is in 'placed' or 'shipped' status.");
        }
    }
}
