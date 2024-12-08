package doit.shop.controller.order;

import doit.shop.controller.order.dto.OrderRequestWrapper;
import doit.shop.controller.order.dto.OrderResponse;
import doit.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequestWrapper requestWrapper) {
        orderService.placeOrder(requestWrapper.orders());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getOrders() {
        List<OrderResponse> orders = orderService.getOrders();
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }
}
