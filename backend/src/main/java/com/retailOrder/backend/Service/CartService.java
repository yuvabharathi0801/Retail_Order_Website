package com.retailOrder.backend.Service;


import com.retailOrder.backend.Model.*;
import com.retailOrder.backend.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public Cart addToCart(Long userId, Long productId, int quantity) {

        // 1. Get or create cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> cartRepository.save(new Cart(userId)));

        // 2. Get product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 3. Check if product already in cart
        Optional<CartItem> existingItem = cart.getItems() == null ?
                Optional.empty() :
                cart.getItems().stream()
                        .filter(item -> item.getProduct().getId().equals(productId))
                        .findFirst();

        if (existingItem.isPresent()) {
            // increase quantity
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            // create new item
            CartItem newItem = new CartItem(cart, product, quantity);
            cartItemRepository.save(newItem);
        }

        return cart;
    }
}