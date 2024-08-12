package com.bookstore.service;

import java.util.List;

import com.bookstore.model.Order;
import com.bookstore.model.Order.Status;
import com.bookstore.model.OrderResponseDTO;

public interface OrderService {
	
    public Order placeOrder(Long userId, Long bookId, int quantity);
    
    public Order updateOrder(Long orderId, Order updatedOrder);
    
    public OrderResponseDTO getOrderById(Long orderId);
    
    public List<Order> getAllOrders();
    
    public void deleteOrder(Long orderId);
    
    public Order updateOrderStatus(Long orderId, Status newStatus);
    
    public Order cancelOrder(Long orderId);

}
