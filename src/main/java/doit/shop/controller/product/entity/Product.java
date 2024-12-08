package doit.shop.controller.product.entity;

import doit.shop.controller.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    private String name;
    private String description;
    private String image;
    private Integer price;
    private Integer stock;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @ManyToOne
//    @JoinColumn(name = "category_id", referencedColumnName = "categoryId", insertable = false, updatable = false)
    Category category;
    @ManyToOne
//    @JoinColumn(name = "user_id", referencedColumnName = "userId", insertable = false, updatable = false)
    UserEntity user;

    @Builder
    public Product(String name, String description, String image, Integer price, Integer stock, Category category, UserEntity user) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.createdAt = LocalDateTime.now();
        this.user = user;
    }

    public void updateProduct(String name, String description, Integer price, Integer stock, String image, Category category) {
        if (name!=null)
            this.name = name;
        if (description!=null)
            this.description = description;
        if (image!=null)
            this.image = image;
        if (price!=null)
            this.price = price;
        if (stock!=null)
            this.stock = stock;
        if(category!=null)
            this.category = category;
        this.modifiedAt = LocalDateTime.now();
    }
}
