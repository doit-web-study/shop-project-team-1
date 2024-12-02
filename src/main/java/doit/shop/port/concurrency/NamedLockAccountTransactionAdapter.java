package doit.shop.port.concurrency;

import doit.shop.domain.Account;
import doit.shop.exception.BusinessException;
import doit.shop.exception.ErrorInfo;
import doit.shop.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

//락과 트랜잭션의 경계를 완벽히 일치?
@Component
@RequiredArgsConstructor
public class NamedLockAccountTransactionAdapter implements AccountTransactionPort {
    private final AccountRepository accountRepository;
    private final NamedLockTemplate namedLockTemplate;

    @Override
    @Transactional
    public void deposit(Long accountId, Integer amount) {
        if (amount <= 0) {
            throw new BusinessException(ErrorInfo.INPUT_ERROR);
        }

        String lockName = "account_lock_" + accountId;

        namedLockTemplate.executeWithLock(lockName, 10, () -> {
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new BusinessException(ErrorInfo.ACCOUNT_NOT_FOUNT));

            account.setInitialBalance(account.getAccountBalance() + amount);
            accountRepository.save(account);
            return null;
        });
    }

    @Override
    @Transactional
    public void withdraw(Long accountId, Integer amount) {
        if (amount <= 0) {
            throw new BusinessException(ErrorInfo.INPUT_ERROR);
        }

        String lockName = "account_lock_" + accountId;

        namedLockTemplate.executeWithLock(lockName, 10, () -> {
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new BusinessException(ErrorInfo.ACCOUNT_NOT_FOUNT));

            if (account.getAccountBalance() < amount) {
                throw new BusinessException(ErrorInfo.PAYMENT_ERROR);
            }

            account.setInitialBalance(account.getAccountBalance() - amount);
            accountRepository.save(account);
            return null;
        });
    }

    @Override
    public LockType getType() {
        return LockType.NAMED_LOCK;
    }
}
