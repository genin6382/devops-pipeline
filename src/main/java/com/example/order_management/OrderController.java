package com.example.order_management;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final Map<Long, Order> orders =
            new LinkedHashMap<>();

    private final AtomicLong idGenerator =
            new AtomicLong(1);

    @GetMapping
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        Order order = orders.get(id);

        if (order == null) {
            throw new NoSuchElementException(
                    "Order not found: " + id);
        }

        return order;
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        long id = idGenerator.getAndIncrement();

        order.setId(id);
        order.setStatus("CREATED");

        orders.put(id, order);
        return order;
    }

    @PutMapping("/{id}")
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
    public Map<String, String> deleteOrder(
            @PathVariable Long id) {

        if (orders.remove(id) == null) {
            throw new NoSuchElementException(
                    "Order not found: " + id);
        }

        return Map.of("message", "Order deleted");
    }
}
