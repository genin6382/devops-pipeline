package com.example.order_management;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Order Management API")
public class OrderController {

    private final Map<Long, Order> orders =
            new LinkedHashMap<>();

    private final AtomicLong idGenerator =
            new AtomicLong(1);

    @GetMapping
    @Operation(summary = "Get all orders", description = "Retrieve a list of all orders")
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieve a specific order by its ID")
    public Order getOrder(@PathVariable Long id) {
        Order order = orders.get(id);

        if (order == null) {
            throw new NoSuchElementException(
                    "Order not found: " + id);
        }

        return order;
    }

    @PostMapping
    @Operation(summary = "Create a new order", description = "Create a new order with the provided details")
    public Order createOrder(@RequestBody Order order) {
        long id = idGenerator.getAndIncrement();

        order.setId(id);
        order.setStatus("CREATED");

        orders.put(id, order);
        return order;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing order", description = "Update the details of an existing order by its ID")
    public Order updateOrder(
            @PathVariable Long id,
            @RequestBody Order updatedOrder) {

        if (!orders.containsKey(id)) {
            throw new NoSuchElementException(
                    "Order not found: " + id);
        }

        updatedOrder.setId(id);
        orders.put(id, updatedOrder);

        return updatedOrder;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an order", description = "Delete an existing order by its ID")
    public Map<String, String> deleteOrder(
            @PathVariable Long id) {

        if (orders.remove(id) == null) {
            throw new NoSuchElementException(
                    "Order not found: " + id);
        }

        return Map.of("message", "Order deleted");
    }
}
