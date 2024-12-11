package doit.shop.controller.order;

import doit.shop.controller.ListWrapper;
import doit.shop.controller.order.dto.OrderProductResponse;
import doit.shop.controller.order.dto.OrderProductsRequest;
import doit.shop.controller.order.entity.Order;
import doit.shop.controller.product.entity.Product;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/orders")
public class OrderController {

    private OrderService orderService;

    @PostMapping("/")
    public void orderProducts(OrderProductsRequest request, HttpServletRequest httpRequest){
        orderService.orderProducts(request, httpRequest);
    }

    @GetMapping("/")
    public ListWrapper<OrderProductResponse> getAllOrders(HttpServletRequest request){
        return orderService.getAllOrders(request);
    }

    @GetMapping("/{id}")
    public OrderProductResponse getOneOrders(@PathVariable Long id, HttpServletRequest request){
        return orderService.getOneOrders(id, request);
    }

    @PostMapping("/{id}/cancel")
    public void cancelOrders(@PathVariable Long id, HttpServletRequest request){
        orderService.cancelOrders(id, request);
    }


}
