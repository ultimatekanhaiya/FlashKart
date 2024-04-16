package com.inventApper.flashkart.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateOrderRequest {

    private String orderStatus = "PENDING";

    private String paymentStatus = "NOT_PAID";

    @NotBlank(message = "billingAddress is required !!")
    private String billingAddress;

    @NotBlank(message = "billingPhone is required !!")
    private String billingPhone;

    @NotBlank(message = "billingName is required !!")
    private String billingName;

    @NotBlank(message = "userId is required !!")
    private String userId;

    @NotBlank(message = "cartId is required !!")
    private String cartId;

}
