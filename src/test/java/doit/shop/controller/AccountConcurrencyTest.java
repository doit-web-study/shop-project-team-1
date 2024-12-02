package doit.shop.controller;

import doit.shop.controller.account.dto.AccountRegisterRequest;
import doit.shop.domain.Account;
import doit.shop.repository.AccountRepository;
import doit.shop.service.AccountService;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountConcurrencyTest {
    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    private Long accountId;
    private static final Logger log = LoggerFactory.getLogger(AccountConcurrencyTest.class);

    @BeforeEach
    void setUp() {
        // 테스트용 계좌 생성
        AccountRegisterRequest request = new AccountRegisterRequest(
                "Test Account",
                "123-456-123111234",
                "Test Bank"
        );
        accountId = accountService.registerAccount(request).accountId();

    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteById(accountId);
    }

    @Test
    void testConcurrentTransactions() throws InterruptedException {
        int initialBalance = 1000;
        int depositAmount = 500;
        int withdrawAmount = 300;
        int threadCount = 10;

        // 초기 잔액 설정
        Account account = accountRepository.findById(accountId).orElseThrow();
        account.setInitialBalance(initialBalance);
        accountRepository.save(account);

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);

        // 스레드 작업 생성
        for (int i = 0; i < threadCount / 2; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await(); // 모든 스레드가 동시에 시작
                    accountService.depositAccount(accountId, depositAmount);
                    log.info("Deposit succeeded in thread: {}", Thread.currentThread().getName());
                } catch (Exception e) {
                    log.warn("Exception occurred during deposit.", e);
                } finally {
                    endLatch.countDown();
                }
            });

            executorService.submit(() -> {
                try {
                    startLatch.await(); // 모든 스레드가 동시에 시작
                    accountService.withdrawAccount(accountId, withdrawAmount);
                    log.info("Withdraw succeeded in thread: {}", Thread.currentThread().getName());
                } catch (Exception e) {
                    log.warn("Exception occurred during withdraw.", e);
                } finally {
                    endLatch.countDown();
                }
            });
        }

        startLatch.countDown(); // 모든 스레드 시작
        endLatch.await(); // 모든 작업이 완료될 때까지 대기
        executorService.shutdown();

        Account updatedAccount = accountRepository.findById(accountId).orElseThrow();
        int expectedBalance = initialBalance + (threadCount / 2) * depositAmount - (threadCount / 2) * withdrawAmount;

        // 최종 잔액이 예상한 값인지 확인
        assertThat(updatedAccount.getAccountBalance()).isEqualTo(expectedBalance);
        log.info("Final balance: {}", updatedAccount.getAccountBalance());
    }

    @Test
    void testConcurrentTransactionsWithPessimisticLocking() throws InterruptedException {
        int initialBalance = 1000;
        int depositAmount = 500;
        int withdrawAmount = 300;
        int threadCount = 10;

        // 초기 잔액 설정
        Account account = accountRepository.findById(accountId).orElseThrow();
        account.setInitialBalance(initialBalance);
        accountRepository.save(account);

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        AtomicLong conflictCount = new AtomicLong(0);

        // 스레드 작업 생성
        for (int i = 0; i < threadCount / 2; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await(); // 모든 스레드가 동시에 시작
                    accountService.depositAccount(accountId, depositAmount);
                    log.info("Deposit succeeded in thread: {}", Thread.currentThread().getName());
                } catch (Exception e) {
                    conflictCount.incrementAndGet();
                    log.warn("Exception occurred. Retrying...", e);
                } finally {
                    endLatch.countDown();
                }
            });

            executorService.submit(() -> {
                try {
                    startLatch.await(); // 모든 스레드가 동시에 시작
                    accountService.withdrawAccount(accountId, withdrawAmount);
                    log.info("Withdraw succeeded in thread: {}", Thread.currentThread().getName());
                } catch (Exception e) {
                    conflictCount.incrementAndGet();
                    log.warn("Exception occurred. Retrying...", e);
                } finally {
                    endLatch.countDown();
                }
            });
        }

        startLatch.countDown(); // 모든 스레드 시작
        endLatch.await(); // 모든 작업이 완료될 때까지 대기
        executorService.shutdown();

        Account updatedAccount = accountRepository.findById(accountId).orElseThrow();
        int expectedBalance = initialBalance + (threadCount / 2) * depositAmount - (threadCount / 2) * withdrawAmount;

        assertThat(updatedAccount.getAccountBalance()).isEqualTo(expectedBalance);
        log.info("Test completed with {} conflicts.", conflictCount.get());
        assertThat(conflictCount.get()).isGreaterThanOrEqualTo(0); // 충돌 발생 확인
    }

    @Test
    void testConcurrentTransactionsWithOptimisticLocking() throws InterruptedException {
        int initialBalance = 1000;
        int depositAmount = 500;
        int withdrawAmount = 300;
        int threadCount = 2;

        // 초기 잔액 설정
        Account account = accountRepository.findById(accountId).orElseThrow();
        account.setInitialBalance(initialBalance);
        accountRepository.save(account);

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        AtomicInteger conflictCount = new AtomicInteger(0);

        // 스레드 작업 생성
        for (int i = 0; i < threadCount / 2; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await(); // 모든 스레드가 동시에 시작
                    accountService.depositAccount(accountId, depositAmount);
                    log.info("Deposit succeeded in thread: {}", Thread.currentThread().getName());
                } catch (OptimisticLockException e) {
                    conflictCount.incrementAndGet();
                    log.warn("Optimistic lock conflict occurred. Retrying...", e);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 현재 스레드의 interrupt 상태 복구
                    log.error("Thread interrupted during await.", e);
                } finally {
                    endLatch.countDown();
                }
            });

            executorService.submit(() -> {
                try {
                    startLatch.await(); // 모든 스레드가 동시에 시작
                    accountService.withdrawAccount(accountId, withdrawAmount);
                    log.info("Withdraw succeeded in thread: {}", Thread.currentThread().getName());
                } catch (OptimisticLockException e) {
                    conflictCount.incrementAndGet();
                    log.warn("Optimistic lock conflict occurred. Retrying...", e);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 현재 스레드의 interrupt 상태 복구
                    log.error("Thread interrupted during await.", e);
                } finally {
                    endLatch.countDown();
                }
            });
        }

        startLatch.countDown(); // 모든 스레드 시작
        endLatch.await(); // 모든 작업이 완료될 때까지 대기
        executorService.shutdown();

        Account updatedAccount = accountRepository.findById(accountId).orElseThrow();
        int expectedBalance = initialBalance + (threadCount / 2) * depositAmount - (threadCount / 2) * withdrawAmount;

        assertThat(updatedAccount.getAccountBalance()).isEqualTo(expectedBalance);
        log.info("Test completed with {} conflicts.", conflictCount.get());
        assertThat(conflictCount.get()).isGreaterThan(0); // 충돌 발생 확인
    }

}
