package com.inventApper.flashkart.dtos;

import com.inventApper.flashkart.entities.CartItem;
import com.inventApper.flashkart.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartDto {

    private String cartId;

    private Date createdDate;

    private UserDto user;

    private List<CartItemDto> items  = new ArrayList<>();
}
