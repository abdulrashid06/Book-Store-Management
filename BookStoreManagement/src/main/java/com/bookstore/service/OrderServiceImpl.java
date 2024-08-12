package com.bookstore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;
    
    

	@Override
	public Order placeOrder(Long userId, Long bookId, int quantity) {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book not found"));

        // Check if sufficient stock is available
        if (book.getStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock available");
        }

        // Reduce book stock
//        book.setStock(book.getStock() - quantity);
//        bookRepository.save(book);

        // Create the order
        Order order = new Order();
        order.setUser(user);
        order.setBook(book);
        order.setQuantity(quantity);
        order.setTotalPrice(book.getPrice() * quantity);
        order.setStatus(Status.PLACED);

        // Add the order to the user's list of orders
        user.getOrders().add(order);
        userRepository.save(user);

        // Save the order
        return orderRepository.save(order);
	}

	@Override
	public Order updateOrder(Long orderId, Order updatedOrder) {
		return null;
	}

	@Override
	public OrderResponseDTO getOrderById(Long orderId) {
	    Order order = orderRepository.findById(orderId)
	            .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

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


	@Override
	public List<Order> getAllOrders() {
		List<Order> orderList = orderRepository.findAll();
		if(orderList.isEmpty()) {
//			throw new OrderNotFoundException("Order list is empty");
			return orderList;
		}
		return orderList;
	}

	@Override
	public void deleteOrder(Long orderId) {
		if (!orderRepository.existsById(orderId)) {
            throw new OrderNotFoundException("Order not found with id: " + orderId);
        }
        orderRepository.deleteById(orderId);
	}
	
	
	@Override
	public Order updateOrderStatus(Long orderId, Status newStatus) {
	    Order order = orderRepository.findById(orderId)
	            .orElseThrow(() -> new OrderNotFoundException("Order not found"));
	    
	    Book book = bookRepository.findById(order.getBook().getId())
	    		.orElseThrow(() -> new BookNotFoundException("Book not found"));
	    
      book.setStock(book.getStock() - order.getQuantity());
      bookRepository.save(book);

	    Status currentStatus = order.getStatus();

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

	    return orderRepository.save(order);
	}
	
	
	
	@Override
	public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if (order.getStatus() == Status.PLACED || order.getStatus() == Status.SHIPPED) {
            order.setStatus(Status.CANCELLED);
            return orderRepository.save(order);
        } else {
            throw new IllegalStateException("Order can only be canceled if it is in 'placed' or 'shipped' status.");
        }
    }


}
