package doit.shop.service;

import doit.shop.controller.order.dto.OrderRequest;
import doit.shop.controller.order.dto.OrderResponse;
import doit.shop.domain.Order;
import doit.shop.domain.Product;
import doit.shop.repository.OrderRepository;
import doit.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void placeOrder(List<OrderRequest> orderRequests) {
        for (OrderRequest orderRequest : orderRequests) {
            Product product = findProductById(orderRequest.productId());

            validateStock(product, orderRequest.numberOfProduct());

            Product updatedProduct = product.withUpdatedStock(
                    product.getStock() - orderRequest.numberOfProduct()
            );

            Order order = new Order(
                    updatedProduct,
                    orderRequest.numberOfProduct(),
                    "ORDERED"
            );

            orderRepository.save(order);
            productRepository.save(updatedProduct);
        }
    }

    public List<OrderResponse> getOrders() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::fromEntity) // OrderResponse의 정적 팩토리 메서드 사용
                .toList();
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = findOrderById(orderId);

        Product updatedProduct = order.getProduct().withUpdatedStock(
                order.getProduct().getStock() + order.getQuantity()
        );

        order.cancel();

        productRepository.save(updatedProduct);
        orderRepository.save(order);
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    private void validateStock(Product product, int requestedQuantity) {
        if (product.getStock() < requestedQuantity) {
            throw new RuntimeException("Insufficient stock");
        }
    }
}
