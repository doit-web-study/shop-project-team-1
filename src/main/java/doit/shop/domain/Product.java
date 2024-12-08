package doit.shop.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "product",
        indexes = {
                // 단일 인덱스 (필터링)
                @Index(name = "idx_product_name", columnList = "name"),
                @Index(name = "idx_product_category_id", columnList = "category_id"),
                // 복합 인덱스 (조건 쿼리)
                @Index(name = "idx_product_category_name", columnList = "category_id, name"),
                @Index(name = "idx_product_category_created_at", columnList = "category_id, created_at")
        }
)
@AllArgsConstructor
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    private String image;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
