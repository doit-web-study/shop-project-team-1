//package doit.shop.controller.account;
//
//import doit.shop.controller.account.entity.AccountEntity;
//import doit.shop.exception.CustomException;
//import doit.shop.exception.ErrorCode;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.dao.OptimisticLockingFailureException;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class AccountServiceTest {
//
//    @Autowired
//    private AccountServiceFacade accountService;
//    @Autowired
//    private AccountRepository accountRepository;
//    @BeforeEach
//    void setUp() {
//            accountRepository.saveAndFlush(
//                    AccountEntity.builder()
//                            .accountName("AccountName")
//                            .accountNumber("AccountNumber")
//                            .accountBankName("Bank")
//                            .userId(1L)
//                            .build()
//            );
//    }
//
//    @AfterEach
//    void tearDown() {
//        accountRepository.deleteAll();
//    }
//    @Test
//    void depositAccount() throws InterruptedException {
//
//        final int threadCount = 10;
//        final ExecutorService executorService = Executors.newFixedThreadPool(10);
//        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);
//
//        for (int i = 0; i < threadCount; i++) {
//            executorService.submit(() -> {
//                try {
//                    accountService.depositAccount(1L,100);
//                } catch (OptimisticLockingFailureException e) {
//                    System.out.println("Optimistic Locking Exception occurred");
//                } catch (InterruptedException e) {
//                    System.out.println("errors");
//                    throw new RuntimeException(e);
//                } finally {
//                    countDownLatch.countDown();
//                }
//            });
//        }
//        countDownLatch.await();
//        final AccountEntity account = accountRepository.findById(1L).orElseThrow();
//
//        // then
//        assertEquals(1000, account.getAccountBalance());
//
//    }
//
//    @Test
//    void withdrawAccount() throws InterruptedException{
//        final int threadCount = 10;
//        final ExecutorService executorService = Executors.newFixedThreadPool(10);
//        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);
//
//        accountService.depositAccount(1L,1100);
//        for (int i = 0; i < threadCount; i++) {
//            executorService.submit(() -> {
//                try {
//                    accountService.withdrawAccount(1L,100);
//                } catch (OptimisticLockingFailureException e) {
//                    System.out.println("Optimistic Locking Exception occurred");
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                } finally {
//                    countDownLatch.countDown();
//                }
//            });
//        }
//        countDownLatch.await();
//        final AccountEntity account = accountRepository.findById(1L).orElseThrow(()-> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
//
//        assertEquals(100, account.getAccountBalance());
//    }
//}