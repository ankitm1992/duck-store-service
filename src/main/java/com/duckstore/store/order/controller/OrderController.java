package com.duckstore.store.order.controller;

import com.duckstore.store.order.model.OrderRequest;
import com.duckstore.store.order.model.OrderResponse;
import com.duckstore.store.order.service.OrderService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public OrderResponse order(@Valid @RequestBody OrderRequest request) {
        return service.quote(request);
    }
}
