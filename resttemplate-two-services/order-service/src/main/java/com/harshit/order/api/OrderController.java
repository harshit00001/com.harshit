package com.harshit.order.api;

import com.harshit.order.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponse place(@RequestBody PlaceOrderRequest request) {
        return orderService.place(request.sku(), request.qty());
    }

    @GetMapping
    public List<OrderResponse> list() {
        return orderService.all();
    }
}
