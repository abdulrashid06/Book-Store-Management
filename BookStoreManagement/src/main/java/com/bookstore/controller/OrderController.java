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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.model.Order;
import com.bookstore.model.Order.Status;
import com.bookstore.model.OrderResponseDTO;
import com.bookstore.service.OrderServiceImpl;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;
    
    
    
    /**
     * Handles the creation of a new order. This endpoint accepts the user ID, 
     * book ID, and the quantity of the book to be ordered. If the order is successfully 
     * created, it returns a response with the created order and an HTTP status of 201 (Created).
     *
     * @param userId The ID of the user placing the order.
     * @param bookId The ID of the book being ordered.
     * @param quantity The quantity of the book to be ordered.
     * @return ResponseEntity containing the created Order and HTTP status 201.
     */
    @PostMapping("create")
    public ResponseEntity<Order> placeOrder(@RequestHeader("Authorization") String token, @RequestParam Long bookId, @RequestParam int quantity) {
        Order order = orderService.placeOrder(token, bookId, quantity);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
    
    
    
    

    /**
     * Retrieves a specific order by its ID. This endpoint returns the order 
     * if found, along with an HTTP status of 200 (OK).
     *
     * @param id The ID of the order to retrieve.
     * @return ResponseEntity containing the requested Order and HTTP status 200.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
    	OrderResponseDTO order = orderService.getOrderById(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    
    
    
    
    /**
     * Retrieves a list of all orders in the system. This endpoint returns a 
     * list of orders, along with an HTTP status of 200 (OK).
     *
     * @return ResponseEntity containing a list of Orders and HTTP status 200.
     */
    @GetMapping("/get_order_list")
    public ResponseEntity<List<Order>> getAllOrders(@RequestHeader("Authorization") String token) {
        List<Order> orders = orderService.getAllOrders(token);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    
    
    
    
    /**
     * Deletes a specific order by its ID. This endpoint deletes the order if it exists 
     * and returns an HTTP status of 204 (No Content) to indicate that the deletion was successful.
     *
     * @param id The ID of the order to delete.
     * @return ResponseEntity with HTTP status 204 indicating that the order was successfully deleted.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    
    
    
    /**
     * Updates the status of an order. This endpoint allows the status to be changed
     * if the new status is a valid transition from the current status. Returns the updated 
     * order with HTTP status 200 (OK) on success, or HTTP status 400 (Bad Request) for invalid transitions.
     *
     * @param orderId The ID of the order to update.
     * @param newStatus The new status to set for the order.
     * @return ResponseEntity with the updated order and HTTP status 200, or HTTP status 400 if the transition is invalid.
     */
    @PutMapping("/status/{orderId}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId, @RequestParam Status newStatus) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    
    
    
    
    /**
     * Cancels an order by its ID. This endpoint cancels the order if it is in a cancellable state
     * and returns HTTP status 204 (No Content) to indicate success. Returns HTTP status 400 (Bad Request)
     * if cancellation is not allowed.
     *
     * @param orderId The ID of the order to cancel.
     * @return ResponseEntity with HTTP status 204 on successful cancellation or HTTP status 400 if cancellation is not allowed.
     */
    @PutMapping("/cancell/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
    	
        try {
            orderService.cancelOrder(orderId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    
    
    
    /**
     * Retrieves all orders with the status 'PLACED'. This endpoint is accessible to 
     * users with the ADMIN role. It returns a list of orders where the status is 
     * set to 'PLACED' and includes a successful HTTP response with status 200 (OK).
     * 
     * @return ResponseEntity containing a list of orders with status 'PLACED' and HTTP status 200.
     */
    @GetMapping("/placed")
    public ResponseEntity<List<Order>> getPlacedOrders() {
        List<Order> orders = orderService.getAllPlacedOrders();
        return ResponseEntity.ok(orders);
    }
    
    
    
    
    /**
     * Retrieves all orders with the status 'SHIPPED'. This endpoint is accessible to 
     * users with the ADMIN role. It returns a list of orders where the status is 
     * set to 'SHIPPED' and includes a successful HTTP response with status 200 (OK).
     * 
     * @return ResponseEntity containing a list of orders with status 'PLACED' and HTTP status 200.
     */
    @GetMapping("/shipped")
    public ResponseEntity<List<Order>> getAllShippedOrders() {
    	List<Order> orders = orderService.getAllShippedOrders();
    	return ResponseEntity.ok(orders);
    }

    
    
    
    /**
     * Retrieves all orders with the status 'DELIVERED'. This endpoint is accessible to 
     * users with the ADMIN role. It returns a list of orders where the status is 
     * set to 'DELIVERED' and includes a successful HTTP response with status 200 (OK).
     * 
     * @return ResponseEntity containing a list of orders with status 'DELIVERED' and HTTP status 200.
     */
    @GetMapping("/delivered")
    public ResponseEntity<List<Order>> getDeliveredOrders() {
        List<Order> orders = orderService.getAllDeliveredOrders();
        return ResponseEntity.ok(orders);
    }

    
}
