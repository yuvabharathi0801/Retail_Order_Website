package com.retailOrder.backend.Service;




import com.retailOrder.backend.Model.*;
import com.retailOrder.backend.Repository.CartItemRepository;
import com.retailOrder.backend.Repository.CartRepository;
import com.retailOrder.backend.Repository.OrderRepository;
import com.retailOrder.backend.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public Order placeOrder(Long userId) {

        // 1. Get Cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> cartItems = cart.getItems();

        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        double totalAmount = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        // 2. Validate stock & calculate total
        for (CartItem cartItem : cartItems) {

            Product product = cartItem.getProduct();

            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException(product.getName() + " is out of stock");
            }

            totalAmount += product.getPrice() * cartItem.getQuantity();
        }

        // 3. Create Order
        Order order = new Order(userId, totalAmount, "PLACED");
        order = orderRepository.save(order);

        // 4. Create OrderItems + reduce stock
        for (CartItem cartItem : cartItems) {

            Product product = cartItem.getProduct();

            // reduce stock
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    product.getPrice()
            );

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);

        // 5. Save order with items
        orderRepository.save(order);

        // 6. Clear Cart
        cartItemRepository.deleteAll(cartItems);

        return order;
    }
}