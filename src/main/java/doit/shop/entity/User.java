package doit.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원 정보를 저장할 엔티티
 */

@Entity // database table과 매핑
@Table(name = "users")
@Getter @Setter @NoArgsConstructor
public class User {

    @Id // basic key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동증가
    private Long id;

    @Column(nullable = false, unique = true) // 중복 허용 x
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true) // 중복 허용 x
    private String email;
}
