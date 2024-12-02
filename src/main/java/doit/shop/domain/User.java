package doit.shop.domain;

import jakarta.persistence.*;
        import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userLoginId;

    @Column(nullable = false)
    private String userPassword;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String userNickname;

    private String userPhoneNumber;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts = new ArrayList<>();

    public static User createUser(String userLoginId,
                                  String userPassword,
                                  String userName,
                                  String userNickname,
                                  String userPhoneNumber) {
        return User.builder()
                .userLoginId(userLoginId)
                .userPassword(userPassword)
                .userName(userName)
                .userNickname(userNickname)
                .userPhoneNumber(userPhoneNumber)
                .build();
    }
}
