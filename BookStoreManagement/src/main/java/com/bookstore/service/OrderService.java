package com.bookstore.service;

import java.util.List;

import com.bookstore.model.Order;
import com.bookstore.model.Order.Status;
import com.bookstore.model.OrderResponseDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface OrderService {
	
    public Order placeOrder(String token, Long bookId, int quantity);
    
    public Order updateOrder(Long orderId, Order updatedOrder);
    
    public OrderResponseDTO getOrderById(Long orderId);
    
    public List<Order> getAllOrders(String token);
    
    public void deleteOrder(Long orderId);
    
    public Order updateOrderStatus(Long orderId, Status newStatus);
    
    public Order cancelOrder(Long orderId);
    
    public List<Order> getAllPlacedOrders();
    
    public List<Order> getAllShippedOrders();

    public List<Order> getAllDeliveredOrders();

}
