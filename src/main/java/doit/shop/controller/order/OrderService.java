package doit.shop.controller.order;

import doit.shop.common.jwt.JwtProvider;
import doit.shop.controller.ListWrapper;
import doit.shop.controller.order.dto.OrderProductRequest;
import doit.shop.controller.order.dto.OrderProductResponse;
import doit.shop.controller.order.dto.OrderProductsRequest;
import doit.shop.controller.order.entity.Order;
import doit.shop.controller.order.entity.OrderStatus;
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

@Service
@RequiredArgsConstructor
public class OrderService {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

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

    public ListWrapper<OrderProductResponse> getAllOrders(HttpServletRequest request) {
    }

    public OrderProductResponse getOneOrders(Long id, HttpServletRequest request) {
    }

    public void cancelOrders(Long id, HttpServletRequest request) {
    }
}
