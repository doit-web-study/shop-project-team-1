package doit.shop.port.concurrency;

public enum LockType {
    NAMED_LOCK,
    OPTIMISTIC,  // 낙관적 잠금
    PESSIMISTIC, // 비관적 잠금
    DEFAULT      // 기본 처리
}
