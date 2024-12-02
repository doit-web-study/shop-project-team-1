package doit.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@Entity
@ToString
public class Account extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String accountName;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private String accountBankName;

    @Column(nullable = false)
    private Integer accountBalance;

    private Account(Builder builder) {
        this.id = builder.id;
        this.accountName = builder.accountName;
        this.accountNumber = builder.accountNumber;
        this.accountBankName = builder.accountBankName;
        this.accountBalance = builder.accountBalance;
    }

    public void setInitialBalance(Integer balance) {
        this.accountBalance = balance;
    }

    public void deposit(Integer amount) {
        this.accountBalance += amount;
    }

    public void withdraw(Integer amount) {
        this.accountBalance -= amount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String accountName;
        private String accountNumber;
        private String accountBankName;
        private Integer accountBalance;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder accountName(String accountName) {
            this.accountName = accountName;
            return this;
        }

        public Builder accountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public Builder accountBankName(String accountBankName) {
            this.accountBankName = accountBankName;
            return this;
        }

        public Builder accountBalance(Integer accountBalance) {
            this.accountBalance = accountBalance;
            return this;
        }

        public Account build() {
            return new Account(this);
        }
    }
}