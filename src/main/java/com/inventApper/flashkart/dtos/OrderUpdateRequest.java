package com.inventApper.flashkart.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderUpdateRequest {

    private String orderStatus;

    private String paymentStatus;

    private String billingName;

    private String billingPhone;

    private String billingAddress;

    private Date deliveredDate;

}
