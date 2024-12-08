package doit.shop.controller.order.dto;

import java.util.List;

public record OrderRequestWrapper(
        List<OrderRequest> orders
) {
}
