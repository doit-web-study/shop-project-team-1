package doit.shop.port.concurrency;

import doit.shop.domain.Account;
import doit.shop.exception.BusinessException;
import doit.shop.exception.ErrorInfo;
import doit.shop.repository.AccountRepository;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OptimisticAccountTransactionAdapter implements AccountTransactionPort {

    private static final Logger log = LoggerFactory.getLogger(OptimisticAccountTransactionAdapter.class);

    private final AccountRepository accountRepository;

    @Override
    public void deposit(Long accountId, Integer amount) {
        validateAmount(amount);
        executeWithRetry(() -> {
            boolean success = false;
            while (!success) {
                try {
                    log.info("Starting deposit operation: accountId={}, amount={}", accountId, amount);

                    Account account = accountRepository.findById(accountId)
                            .orElseThrow(() -> new BusinessException(ErrorInfo.ACCOUNT_NOT_FOUNT));

                    log.info("Fetched account before deposit: {}", account);

                    account.deposit(amount);
                    Account savedAccount = accountRepository.saveAndFlush(account);

                    log.info("Completed deposit operation: updatedAccount={}", savedAccount);

                    // Verify consistency
                    verifyAccountConsistency(accountId, savedAccount.getAccountBalance());
                    success = true;
                } catch (OptimisticLockException e) {
                    log.warn("Optimistic lock conflict during deposit operation. Retrying...");
                }
            }
        });
    }

    @Override
    public void withdraw(Long accountId, Integer amount) {
        validateAmount(amount);
        executeWithRetry(() -> {
            boolean success = false;
            while (!success) {
                try {
                    log.info("Starting withdraw operation: accountId={}, amount={}", accountId, amount);

                    Account account = accountRepository.findById(accountId)
                            .orElseThrow(() -> new BusinessException(ErrorInfo.ACCOUNT_NOT_FOUNT));

                    log.info("Fetched account before withdrawal: {}", account);

                    if (account.getAccountBalance() < amount) {
                        log.warn("Insufficient balance for withdrawal: accountId={}, balance={}, withdrawAmount={}",
                                accountId, account.getAccountBalance(), amount);
                        throw new BusinessException(ErrorInfo.PAYMENT_ERROR);
                    }

                    account.withdraw(amount);
                    Account savedAccount = accountRepository.saveAndFlush(account);

                    log.info("Completed withdrawal operation: updatedAccount={}", savedAccount);

                    // Verify consistency
                    verifyAccountConsistency(accountId, savedAccount.getAccountBalance());
                    success = true;
                } catch (OptimisticLockException e) {
                    log.warn("Optimistic lock conflict during withdrawal operation. Retrying...");
                }
            }
        });
    }

    @Override
    public LockType getType() {
        return LockType.OPTIMISTIC;
    }

    private void validateAmount(Integer amount) {
        if (amount <= 0) {
            log.warn("Invalid amount for transaction: amount={}", amount);
            throw new BusinessException(ErrorInfo.INPUT_ERROR);
        }
    }

    private void executeWithRetry(Runnable action) {
        int retryCount = 0;
        int maxRetries = 100;
        long backoffTime = 30; // 초기 대기 시간

        while (retryCount < maxRetries) {
            try {
                action.run();
                log.info("Transaction succeeded on attempt: {}", retryCount + 1);
                return;
            } catch (OptimisticLockException e) {
                retryCount++;
                log.warn("Optimistic lock conflict. Retrying... Attempt: {}", retryCount);

                if (retryCount >= maxRetries) {
                    log.error("Max retries reached. Transaction failed.");
                    throw new BusinessException(ErrorInfo.CONCURRENCY_ERROR);
                }

                try {
                    Thread.sleep(backoffTime);
                    backoffTime = Math.min(backoffTime * 2, 5000); // 최대 백오프 시간을 5초로 제한
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    log.error("Thread interrupted during retry backoff.", interruptedException);
                    throw new BusinessException(ErrorInfo.CONCURRENCY_ERROR);
                }
            }
        }
    }

    private void verifyAccountConsistency(Long accountId, Integer expectedBalance) {
        Account verifiedAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new BusinessException(ErrorInfo.ACCOUNT_NOT_FOUNT));
        if (!verifiedAccount.getAccountBalance().equals(expectedBalance)) {
            log.error("Account balance inconsistency detected: expected={}, actual={}", expectedBalance, verifiedAccount.getAccountBalance());
            throw new BusinessException(ErrorInfo.CONCURRENCY_ERROR);
        }
        log.info("Account balance verified successfully: accountId={}, balance={}", accountId, verifiedAccount.getAccountBalance());
    }
}
