package doit.shop.controller.order;

import doit.shop.controller.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {

    Order findByOrderId(Long orderId);
}
