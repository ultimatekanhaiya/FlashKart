package com.inventApper.flashkart.services;

import com.inventApper.flashkart.dtos.AddItemToCartRequest;
import com.inventApper.flashkart.dtos.CartDto;

public interface CartService {

    //add item to cart
    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    //remove item from cart
    void removeItemFromCart(String userId, int cartItem);

    //clear cart
    void clearCart(String userId);

    //
    CartDto getCartByUser(String userId);
}
