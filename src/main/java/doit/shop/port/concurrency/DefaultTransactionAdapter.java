package doit.shop.port.concurrency;

import doit.shop.domain.Account;
import doit.shop.exception.BusinessException;
import doit.shop.exception.ErrorInfo;
import doit.shop.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultTransactionAdapter implements AccountTransactionPort {
    private final AccountRepository accountRepository;

    @Override
    public void deposit(Long accountId, Integer amount) {
        if (amount <= 0) {
            throw new BusinessException(ErrorInfo.INPUT_ERROR);
        }
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new BusinessException(ErrorInfo.ACCOUNT_NOT_FOUNT));

        Account updatedAccount = Account.builder()
                .id(account.getId())
                .accountName(account.getAccountName())
                .accountNumber(account.getAccountNumber())
                .accountBankName(account.getAccountBankName())
                .accountBalance(account.getAccountBalance() + amount)
                .build();

        accountRepository.save(updatedAccount);
    }

    @Override
    public void withdraw(Long accountId, Integer amount) {
        if (amount <= 0) {
            throw new BusinessException(ErrorInfo.INPUT_ERROR);
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new BusinessException(ErrorInfo.ACCOUNT_NOT_FOUNT));

        if (account.getAccountBalance() < amount) {
            throw new BusinessException(ErrorInfo.PAYMENT_ERROR);
        }

        Account updatedAccount = Account.builder()
                .id(account.getId())
                .accountName(account.getAccountName())
                .accountNumber(account.getAccountNumber())
                .accountBankName(account.getAccountBankName())
                .accountBalance(account.getAccountBalance() - amount)
                .build();

        accountRepository.save(updatedAccount);
    }

    @Override
    public LockType getType() {
        return LockType.DEFAULT;
    }
}
