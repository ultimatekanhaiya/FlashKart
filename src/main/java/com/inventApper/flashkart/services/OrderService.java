package com.inventApper.flashkart.services;

import com.inventApper.flashkart.dtos.CreateOrderRequest;
import com.inventApper.flashkart.dtos.OrderDto;
import com.inventApper.flashkart.dtos.OrderUpdateRequest;
import com.inventApper.flashkart.dtos.PageableResponse;

import java.util.List;

public interface OrderService {

    //create order
    OrderDto createOrder(CreateOrderRequest orderRequest);

    //remove order
    void removeOrder(String orderId);

    //get orders of user
    List<OrderDto> getOrdersByUser(String userId);

    //get orders
    PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir);

    //update order
    OrderDto updateOrder(String orderId,OrderUpdateRequest request);
}
