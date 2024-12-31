package doit.shop.domain.account.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    private String accountName;
    private String accountNumber;
    private String accountBankName;
    private Integer accountBalance;

    @Builder
    public Account(String accountName, String accountNumber, String accountBankName, Integer accountBalance) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.accountBankName = accountBankName;
        this.accountBalance = accountBalance;
    }
}
