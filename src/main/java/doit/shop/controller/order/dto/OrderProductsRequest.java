package doit.shop.controller.order.dto;

import doit.shop.controller.ListWrapper;
import lombok.Getter;

@Getter
public class OrderProductsRequest {
    ListWrapper<OrderProductRequest> orders;

}
