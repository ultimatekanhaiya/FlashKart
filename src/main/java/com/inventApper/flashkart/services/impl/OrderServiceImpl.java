package com.inventApper.flashkart.services.impl;

import com.inventApper.flashkart.dtos.CreateOrderRequest;
import com.inventApper.flashkart.dtos.OrderDto;
import com.inventApper.flashkart.dtos.OrderUpdateRequest;
import com.inventApper.flashkart.dtos.PageableResponse;
import com.inventApper.flashkart.entities.*;
import com.inventApper.flashkart.exceptions.BadApiRequestException;
import com.inventApper.flashkart.exceptions.ResourceNotFoundException;
import com.inventApper.flashkart.helper.Helper;
import com.inventApper.flashkart.repositories.CartRepository;
import com.inventApper.flashkart.repositories.OrderRepository;
import com.inventApper.flashkart.repositories.UserRepository;
import com.inventApper.flashkart.services.OrderService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper mapper;

    Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public OrderDto getOrder(String orderId) {
        Order order = this.orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found !!"));
        return this.mapper.map(order, OrderDto.class);
    }

    @Override
    public OrderDto createOrder(CreateOrderRequest orderRequest) {
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", orderRequest.getUserId()));
        Cart cart = cartRepository.findById(orderRequest.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", orderRequest.getCartId()));
        List<CartItem> cartItems = cart.getItems();

        if (cartItems.size() <= 0) {
            throw new BadApiRequestException("Cart is empty");
        }
        Order order = Order.builder()
                .billingName(orderRequest.getBillingName())
                .billingPhone(orderRequest.getBillingPhone())
                .billingAddress(orderRequest.getBillingAddress())
                .orderedDate(new Date())
                .deliveredDate(null)
                .paymentStatus(orderRequest.getPaymentStatus())
                .orderStatus(orderRequest.getOrderStatus())
                .OrderId(UUID.randomUUID().toString())
                .user(user)
                .build();

        AtomicInteger orderAmount = new AtomicInteger(0);

        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            OrderItem orderItem = OrderItem.builder()
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .order(order).build();

            orderAmount.addAndGet(orderItem.getTotalPrice());
            return orderItem;
        }).toList();

        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());

        // clear cart items
        cart.getItems().clear();
        cartRepository.save(cart);

        Order savedOrder = orderRepository.save(order);

        return mapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getOrdersByUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        List<Order> orders = orderRepository.findByUser(user);
        List<OrderDto> orderDtos = orders.stream().map(order -> mapper.map(order, OrderDto.class)).toList();
        return orderDtos;
    }

    @Override
    public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Order> page = orderRepository.findAll(pageable);
        return Helper.getPageableResponse(page, OrderDto.class);
    }

    @Override
    public OrderDto updateOrder(String orderId, OrderUpdateRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        order.setBillingName(request.getBillingName());
        order.setBillingPhone(request.getBillingPhone());
        order.setBillingAddress(request.getBillingAddress());
        order.setPaymentStatus(request.getPaymentStatus());
        order.setOrderStatus(request.getOrderStatus());
        order.setDeliveredDate(request.getDeliveredDate());
        Order updatedOrder = orderRepository.save(order);
        return mapper.map(updatedOrder, OrderDto.class);
    }

    @Override
    public OrderDto updateOrder(String orderId, OrderDto request) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new BadApiRequestException("Invalid update data"));
        order.setBillingName(request.getBillingName());
        order.setBillingPhone(request.getBillingPhone());
        order.setBillingAddress(request.getBillingAddress());
        order.setPaymentStatus(request.getPaymentStatus());
        order.setOrderStatus(request.getOrderStatus());
        order.setDeliveredDate(request.getDeliveredDate());
        order.setRazorPayOrderId(request.getRazorPayOrderId());
        order.setPaymentId(request.getPaymentId());
        Order updatedOrder = orderRepository.save(order);
        return mapper.map(updatedOrder, OrderDto.class);
    }
}
