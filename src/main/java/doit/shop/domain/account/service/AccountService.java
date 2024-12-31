package doit.shop.domain.account.service;

import doit.shop.domain.account.entity.Account;
import doit.shop.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public void deposit(Long accountId, Integer amount) throws AccountNotFoundException{
        Account account = accountRepository.findByIdWithLock(accountId)
                        .orElseThrow(() -> new AccountNotFoundException("[accountId: " + accountId + "] 는 찾을 수 없음"));
        account.setAccountBalance(account.getAccountBalance() + amount);
        accountRepository.save(account);
    }

    @Transactional
    public void withdraw(Long accountId, Integer amount) throws AccountNotFoundException{
        Account account = accountRepository.findByIdWithLock(accountId)
                .orElseThrow(() -> new AccountNotFoundException("[accountId: " + accountId + "] 는 찾을 수 없음"));
        account.setAccountBalance(account.getAccountBalance() - amount);
        accountRepository.save(account);
    }
}
