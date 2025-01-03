package doit.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber; // 계좌 번호

    @Column(nullable = false)
    private String accountName; // 계좌 애칭

    @Column(nullable = false)
    private String accountBankName; // 은행 이름

    @Column(nullable = false)
    private Integer accountBalance = 0; // 초기 잔액 (0)

    @Version // 동시성 문제 방지
    private Long version;
}
