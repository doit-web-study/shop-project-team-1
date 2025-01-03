package doit.shop.controller.account;

import doit.shop.controller.account.dto.*;
import doit.shop.entity.Account;
import doit.shop.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public AccountIdResponse registerAccount(AccountRegisterRequest request) {
        Account account = new Account();
        account.setAccountNumber(request.accountNumber());
        account.setAccountName(request.accountName());
        account.setAccountBankName(request.accountBankName());
        account = accountRepository.save(account);
        return new AccountIdResponse(account.getId());
    }

    public List<AccountInfoResponse> getAccountList() {
        return accountRepository.findAll().stream()
                .map(account -> new AccountInfoResponse(
                        account.getId(),
                        account.getAccountName(),
                        account.getAccountNumber(),
                        account.getAccountBankName(),
                        account.getAccountBalance()
                ))
                .collect(Collectors.toList());
    }

    public AccountInfoResponse getAccountInfo(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        return new AccountInfoResponse(
                account.getId(),
                account.getAccountName(),
                account.getAccountNumber(),
                account.getAccountBankName(),
                account.getAccountBalance()
        );
    }

    @Transactional
    public AccountInfoResponse updateAccountInfo(Long accountId, AccountUpdateRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.setAccountName(request.accountName());
        account = accountRepository.save(account);
        return new AccountInfoResponse(
                account.getId(),
                account.getAccountName(),
                account.getAccountNumber(),
                account.getAccountBankName(),
                account.getAccountBalance()
        );
    }

    @Transactional
    public void depositAccount(Long accountId, Integer amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.setAccountBalance(account.getAccountBalance() + amount);
        accountRepository.save(account);
    }

    @Transactional
    public void withdrawAccount(Long accountId, Integer amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        if (account.getAccountBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        account.setAccountBalance(account.getAccountBalance() - amount);
        accountRepository.save(account);
    }
}
