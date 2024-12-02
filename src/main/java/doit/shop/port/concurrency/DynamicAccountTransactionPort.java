package doit.shop.port.concurrency;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
public class DynamicAccountTransactionPort {

    private final Map<LockType, AccountTransactionPort> portMap;

    // 모든 구현체를 주입받아 Map으로 초기화
    public DynamicAccountTransactionPort(List<AccountTransactionPort> ports) {
        this.portMap = ports.stream()
                .collect(Collectors.toMap(AccountTransactionPort::getType, port -> port));
    }

    private AccountTransactionPort getPort(LockType type) {
        if (!portMap.containsKey(type)) {
            throw new NoSuchElementException("No AccountTransactionPort found for type: " + type);
        }
        return portMap.get(type);
    }


    public void deposit(Long accountId, Integer amount, LockType type) {
        getPort(type).deposit(accountId, amount);
    }

    public void withdraw(Long accountId, Integer amount, LockType type) {
        getPort(type).withdraw(accountId, amount);
    }
}