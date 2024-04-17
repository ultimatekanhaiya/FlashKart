package com.inventApper.flashkart.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderDto {

    private String OrderId;

    private String orderStatus="PENDING";

    private String paymentStatus="NOT_PAID";

    private int orderAmount;

    private String billingAddress;

    private String billingPhone;

    private String billingName;

    private Date OrderedDate = new Date();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date deliveredDate;

    private List<OrderItemDto> orderItems = new ArrayList<>();

    private String razorPayOrderId;

    private String paymentId;

    private UserDto user;
}
