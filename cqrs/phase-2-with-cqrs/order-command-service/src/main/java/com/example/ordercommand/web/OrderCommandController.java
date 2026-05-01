package com.example.ordercommand.web;

import com.example.ordercommand.dto.CreateOrderRequest;
import com.example.ordercommand.dto.OrderResponse;
import com.example.ordercommand.dto.UpdateStatusRequest;
import com.example.ordercommand.service.OrderCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderCommandController {

    private final OrderCommandService orderCommandService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@RequestBody CreateOrderRequest request) {
        return orderCommandService.createOrder(request);
    }

    @PutMapping("/{id}/status")
    public OrderResponse updateStatus(@PathVariable Long id, @RequestBody UpdateStatusRequest request) {
        return orderCommandService.updateStatus(id, request);
    }
}
