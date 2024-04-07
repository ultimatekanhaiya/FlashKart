package com.inventApper.flashkart.dtos;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderItemDto {

    private int orderItemId;

    private int quantity;

    private int totalPrice;

    private ProductDto product;
}
