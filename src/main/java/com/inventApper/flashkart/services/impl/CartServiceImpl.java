package com.inventApper.flashkart.services.impl;

import com.inventApper.flashkart.dtos.AddItemToCartRequest;
import com.inventApper.flashkart.dtos.CartDto;
import com.inventApper.flashkart.entities.Cart;
import com.inventApper.flashkart.entities.CartItem;
import com.inventApper.flashkart.entities.Product;
import com.inventApper.flashkart.entities.User;
import com.inventApper.flashkart.exceptions.BadApiRequestException;
import com.inventApper.flashkart.exceptions.ResourceNotFoundException;
import com.inventApper.flashkart.repositories.CartItemRepository;
import com.inventApper.flashkart.repositories.CartRepository;
import com.inventApper.flashkart.repositories.ProductRepository;
import com.inventApper.flashkart.repositories.UserRepository;
import com.inventApper.flashkart.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {

        if (request.getQuantity() <= 0) {
            throw new BadApiRequestException("Quantity must be greater than 0");
        }
        AtomicBoolean updated = new AtomicBoolean(false);

        int quantity = request.getQuantity();
        String productId = request.getProductId();

        //fetch product
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        //fetch user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Cart cart = null;
        try {
            //fetch cart if already exists
            cart = cartRepository.findByUser(user).get();
        } catch (NoSuchElementException ex) {
            //else create a new cart
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedDate(new Date());
        }
        //perform cart operations
        //if cart item already exists
        List<CartItem> updatedItems = cart.getItems();
        updatedItems = updatedItems.stream().map(item -> {

            if (item.getProduct().getProductId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                item.setTotalPrice(item.getQuantity() * product.getDiscountedPrice());
                updated.set(true);
            }
            return item;
        }).toList();

//        cart.setItems(updatedItems);

        if (!updated.get()) {
            //create items
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .product(product)
                    .cart(cart)
                    .build();

            //add items
            cart.getItems().add(cartItem);
        }

        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);

        return mapper.map(updatedCart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("Cart item not found in DB"));
        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart of given user not found in DB"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart of given user not found in DB"));
        return mapper.map(cart, CartDto.class);
    }
}
