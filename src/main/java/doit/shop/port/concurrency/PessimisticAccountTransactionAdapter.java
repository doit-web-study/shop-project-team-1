package doit.shop.port.concurrency;

import doit.shop.domain.Account;
import doit.shop.exception.BusinessException;
import doit.shop.exception.ErrorInfo;
import doit.shop.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PessimisticAccountTransactionAdapter implements AccountTransactionPort {
    private final AccountRepository accountRepository;

    @Override
    public void deposit(Long accountId, Integer amount) {
        if (amount <= 0) {
            throw new BusinessException(ErrorInfo.INPUT_ERROR);
        }

        Account account = accountRepository.findByIdWithLock(accountId)
                .orElseThrow(() -> new BusinessException(ErrorInfo.ACCOUNT_NOT_FOUNT));
        account.deposit(amount);
        accountRepository.save(account); // 비관적 잠금 처리
    }

    @Override
    public void withdraw(Long accountId, Integer amount) {
        if (amount <= 0) {
            throw new BusinessException(ErrorInfo.INPUT_ERROR);
        }

        Account account = accountRepository.findByIdWithLock(accountId)
                .orElseThrow(() -> new BusinessException(ErrorInfo.ACCOUNT_NOT_FOUNT));

        if (account.getAccountBalance() < amount) {
            throw new BusinessException(ErrorInfo.PAYMENT_ERROR);
        }

        account.withdraw(amount);
        accountRepository.save(account); // 비관적 잠금 처리
    }

    @Override
    public LockType getType() {
        return LockType.PESSIMISTIC;
    }
}
