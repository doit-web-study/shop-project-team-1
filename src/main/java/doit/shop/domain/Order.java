package doit.shop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Long totalPrice;

    @Column(nullable = false)
    private String status; // 주문 상태 (e.g., "ORDERED", "CANCELLED")

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Order(Product product, Integer quantity, String status) {
        this.product = product;
        this.quantity = quantity;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.totalPrice = (long) (product.getPrice() * quantity); // 총 금액 계산
    }

    public void cancel() {
        this.status = "CANCELLED";
    }
}
