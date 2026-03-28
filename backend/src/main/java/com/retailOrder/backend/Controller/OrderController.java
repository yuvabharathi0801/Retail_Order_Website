package com.retailOrder.backend.Controller;



import com.retailOrder.backend.Model.Order;
import com.retailOrder.backend.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Place Order
    @PostMapping("/place")
    public Order placeOrder(@RequestParam Long userId) {
        return orderService.placeOrder(userId);
    }
}