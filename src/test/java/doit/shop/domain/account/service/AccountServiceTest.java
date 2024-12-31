package doit.shop.domain.account.service;

import doit.shop.domain.account.entity.Account;
import doit.shop.domain.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @Transactional
    void testPessimisticLocking() throws InterruptedException, AccountNotFoundException{

        Account account = Account.builder()
                .accountName("주거래")
                .accountNumber("123456789")
                .accountBankName("농협")
                .accountBalance(1000)
                .build();

        Account savedAccount = accountRepository.save(account);


        System.out.println("Saved Account ID: " + savedAccount.getId());
        Optional<Account> optionalAccount = accountRepository.findById(1L);
        System.out.println("Account Present: " + optionalAccount.isPresent());



        Thread depositThread = new Thread(() -> {
            try {
                System.out.println("Deposit Account ID: " + savedAccount.getId());
                accountService.deposit(savedAccount.getId(), 500);
            } catch (AccountNotFoundException e) {
                e.printStackTrace();
            }
        });

        Thread withdrawThread = new Thread(() -> {
            try {
                System.out.println("Deposit Account ID: " + savedAccount.getId());
                accountService.withdraw(savedAccount.getId(), 300);
            } catch (AccountNotFoundException e) {
                e.printStackTrace();
            }
        });

        depositThread.start();
        withdrawThread.start();


        depositThread.join();
        withdrawThread.join();

        Account updatedAccount = accountRepository.findByIdWithLock(account.getId())
                .orElseThrow(() -> new AccountNotFoundException("[accountId: " + account.getId() + "] 는 찾을 수 없음"));

        assertEquals(1200, updatedAccount.getAccountBalance());
    }

}