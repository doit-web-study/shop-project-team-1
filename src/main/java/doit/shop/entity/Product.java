package doit.shop.entity;


import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

@Entity
@Table(
        name = "product",
        indexes = {
                @Index(name = "idx_product_category", columnList = "category")
        }
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    // Constructors, getters, setters...
}