
package com.example.order_management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderControllerTest {

    private OrderController controller;

    @BeforeEach
    void setUp() {
        controller = new OrderController();
    }

    @Test
    void createOrderShouldAssignIdAndCreatedStatus() {
        Order order = new Order(
                null, "Keyboard", 2, null);

        Order created = controller.createOrder(order);

        assertEquals(1L, created.getId());
        assertEquals("Keyboard", created.getProductName());
        assertEquals(2, created.getQuantity());
        assertEquals("CREATED", created.getStatus());
    }

    @Test
    void getAllOrdersShouldReturnCreatedOrders() {
        controller.createOrder(
                new Order(null, "Mouse", 1, null));

        List<Order> orders = controller.getAllOrders();

        assertEquals(1, orders.size());
        assertEquals("Mouse", orders.get(0).getProductName());
    }

    @Test
    void getOrderShouldReturnCorrectOrder() {
        controller.createOrder(
                new Order(null, "Monitor", 1, null));

        Order result = controller.getOrder(1L);

        assertEquals("Monitor", result.getProductName());
    }

    @Test
    void updateOrderShouldReplaceOrderDetails() {
        controller.createOrder(
                new Order(null, "Mouse", 1, null));

        Order updated = controller.updateOrder(
                1L, new Order(null, "Gaming Mouse", 2, null));

        assertEquals(1L, updated.getId());
        assertEquals("Gaming Mouse", updated.getProductName());
        assertEquals(2, updated.getQuantity());
    }

    @Test
    void deleteOrderShouldRemoveOrder() {
        controller.createOrder(
                new Order(null, "Keyboard", 1, null));

        Map<String, String> result = controller.deleteOrder(1L);

        assertEquals("Order deleted", result.get("message"));
        assertTrue(controller.getAllOrders().isEmpty());
    }
}
