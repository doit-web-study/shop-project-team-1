package doit.shop.controller.account.entity;

import doit.shop.controller.account.dto.AccountUpdateRequest;
import doit.shop.controller.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long accountId;
    private String accountName;
    private String accountNumber;
    private String accountBankName;
    @Column(columnDefinition = "INTEGER")
    private Integer accountBalance;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    @JoinColumn(name = "userId", referencedColumnName = "userId", insertable = false, updatable = false)
    Long userId;

    // 낙관적 락
//    @Version
//    private Long version;

    @Builder
    public AccountEntity(String accountName, String accountNumber, String accountBankName, Long userId) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.accountBankName = accountBankName;
        this.accountBalance = 0;
        this.createdAt = LocalDateTime.now();
        this.userId = userId;
    }

    public void updateName(String accountName) {
        if(accountName!=null) {
            this.accountName = accountName;
            this.modifiedAt = LocalDateTime.now();
        }
    }

    public void deposit(Integer amount) {
        this.accountBalance += amount;
    }

    public void withdraw(Integer amount){
        this.accountBalance-=amount;
    }
}
