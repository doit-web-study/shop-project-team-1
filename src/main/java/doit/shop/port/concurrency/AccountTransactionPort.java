package doit.shop.port.concurrency;

public interface AccountTransactionPort {
    void deposit(Long accountId, Integer amount);
    void withdraw(Long accountId, Integer amount);
    LockType getType(); // 각 구현체가 자신만의 타입을 반환
}
