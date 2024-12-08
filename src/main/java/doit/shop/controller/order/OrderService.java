package doit.shop.controller.order;

import doit.shop.common.jwt.JwtProvider;
import doit.shop.controller.ListWrapper;
import doit.shop.controller.order.dto.OrderProductRequest;
import doit.shop.controller.order.dto.OrderProductResponse;
import doit.shop.controller.order.dto.OrderProductsRequest;
import doit.shop.controller.order.dto.OrderResponse;
import doit.shop.controller.order.entity.Order;
import doit.shop.controller.order.entity.OrderStatus;
import doit.shop.controller.product.dto.ProductInfoResponse;
import doit.shop.controller.product.entity.Product;
import doit.shop.controller.product.repository.ProductRepository;
import doit.shop.controller.user.UserRepository;
import doit.shop.controller.user.entity.UserEntity;
import doit.shop.exception.CustomException;
import doit.shop.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public void orderProducts(OrderProductsRequest request, HttpServletRequest httpRequest) {
        String accessToken = jwtProvider.resolveToken(httpRequest);
        String userLoginId = jwtProvider.getUserId(accessToken);
        UserEntity user = userRepository.findByLoginId(userLoginId);

        if(!jwtProvider.isValidToken(accessToken,userLoginId))
            throw new CustomException(ErrorCode.NO_TOKEN_EXIST);

        List<OrderProductRequest> results = request.getOrders().result();
        results.stream().map(result -> {

            Product product = productRepository.findByProductId(result.getProductId());
            if(product==null){
                throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
            }

            return Order.builder()
                    .orderStatus(OrderStatus.ORDERED)
                    .orderedStock(result.getNumberOfProducts())
                    .totalPrice(result.getNumberOfProducts())
                    .user(user)
                    .product(product)
                    .build();
        });

    }

    public OrderProductResponse getOrder(Long orderId, UserEntity user){
        Order order = orderRepository.findByOrderId(orderId);
        Product product = productRepository.findByProductId(order.getProduct().getProductId());

        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getOrderId())
                .orderCreatedAt(order.getCreatedAt())
                .orderStatus(order.getOrderStatus())
                .numberOfProduct(order.getOrderedStock())
                .orderTotalPrice(order.getTotalPrice())
                .build();

        ProductInfoResponse productInfoResponse = ProductInfoResponse.builder()
                .userId(user.getUserId())
                .userNickname(user.getNickname())
                .productId(product.getProductId())
                .productName(product.getName())
                .productStock(product.getStock())
                .productPrice(product.getPrice())
                .productDescription(product.getDescription())
                .categoryId(product.getCategory().getCategoryId())
                .createdAt(product.getCreatedAt())
                .modifiedAt(product.getModifiedAt())
                .productImage(product.getImage())
                .categoryType(product.getCategory().getCategoryType())
                .build();

        return OrderProductResponse.builder()
                .orderResponse(orderResponse)
                .productInfoResponse(productInfoResponse)
                .build();
    }

    public ListWrapper<OrderProductResponse> getAllOrders(HttpServletRequest request) {
        String accessToken = jwtProvider.resolveToken(request);
        String userLoginId = jwtProvider.getUserId(accessToken);
        UserEntity user = userRepository.findByLoginId(userLoginId);

        if(!jwtProvider.isValidToken(accessToken,userLoginId))
            throw new CustomException(ErrorCode.NO_TOKEN_EXIST);

        List<Order> orders = orderRepository.findAll();
        List<OrderProductResponse> orderProductResponses = orders.stream().map(o ->
        {
            return getOrder(o.getOrderId(), user);
        }).collect(Collectors.toList());

        return new ListWrapper<>(orderProductResponses);
    }

    public OrderProductResponse getOneOrders(Long orderId, HttpServletRequest request) {
        String accessToken = jwtProvider.resolveToken(request);
        String userLoginId = jwtProvider.getUserId(accessToken);
        UserEntity user = userRepository.findByLoginId(userLoginId);

        if(!jwtProvider.isValidToken(accessToken,userLoginId))
            throw new CustomException(ErrorCode.NO_TOKEN_EXIST);

        return getOrder(orderId,user);
    }

    public void cancelOrders(Long id, HttpServletRequest request) {
        String accessToken = jwtProvider.resolveToken(request);
        String userLoginId = jwtProvider.getUserId(accessToken);
        UserEntity user = userRepository.findByLoginId(userLoginId);

        if(!jwtProvider.isValidToken(accessToken,userLoginId))
            throw new CustomException(ErrorCode.NO_TOKEN_EXIST);
    }
}
