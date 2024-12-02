package doit.shop.service;

import doit.shop.controller.account.dto.AccountInfoResponse;
import doit.shop.controller.account.dto.AccountRegisterRequest;
import doit.shop.controller.account.dto.AccountUpdateRequest;
import doit.shop.domain.Account;
import doit.shop.exception.BusinessException;
import doit.shop.exception.ErrorInfo;
import doit.shop.port.concurrency.DynamicAccountTransactionPort;
import doit.shop.port.concurrency.LockType;
import doit.shop.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final DynamicAccountTransactionPort dynamicAccountTransactionPort;

    public AccountInfoResponse registerAccount(AccountRegisterRequest request) {
        Account account = Account.builder()
                .accountName(request.accountName())
                .accountBankName(request.accountBankName())
                .accountNumber(request.accountNumber())
                .accountBalance(0)
                .build();
        Account savedAccount = accountRepository.save(account);
        return AccountInfoResponse.fromEntity(savedAccount);
    }

    public List<AccountInfoResponse> getAccountList() {
        return accountRepository.findAll().stream()
                .map(AccountInfoResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public AccountInfoResponse getAccountInfo(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new BusinessException(ErrorInfo.ACCOUNT_NOT_FOUNT));
        return AccountInfoResponse.fromEntity(account);
    }

    public AccountInfoResponse updateAccountInfo(Long accountId, AccountUpdateRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new BusinessException(ErrorInfo.ACCOUNT_NOT_FOUNT));

        Account updatedAccount = Account.builder()
                .id(account.getId())
                .accountName(request.accountName())
                .accountNumber(account.getAccountNumber())
                .accountBankName(account.getAccountBankName())
                .accountBalance(account.getAccountBalance())
                .build();

        Account savedAccount = accountRepository.save(updatedAccount);

        return AccountInfoResponse.fromEntity(savedAccount);
    }

//    @Retryable(
//            value = {CannotAcquireLockException.class},
//            maxAttempts = 3,
//            backoff = @Backoff(delay = 200)
//    )
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void depositAccount(Long accountId, Integer amount) {
        dynamicAccountTransactionPort.deposit(accountId, amount, LockType.PESSIMISTIC);
    }

//    @Retryable(
//            value = {CannotAcquireLockException.class},
//            maxAttempts = 3,
//            backoff = @Backoff(delay = 200)
//    )
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void withdrawAccount(Long accountId, Integer amount) {
        dynamicAccountTransactionPort.withdraw(accountId, amount, LockType.PESSIMISTIC);
    }
}
