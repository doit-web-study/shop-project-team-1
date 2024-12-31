package doit.shop.domain.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue
    private Long id;

    private String type;

    @Builder
    public Category(String type) {
        this.type = type;
    }
}
