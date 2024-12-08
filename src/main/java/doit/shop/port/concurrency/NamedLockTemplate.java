package doit.shop.port.concurrency;

import doit.shop.exception.BusinessException;
import doit.shop.exception.ErrorInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class NamedLockTemplate {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final String GET_LOCK = "SELECT GET_LOCK(:userLockName, :timeoutSeconds)";
    private static final String RELEASE_LOCK = "SELECT RELEASE_LOCK(:userLockName)";

    @Transactional
    public <T> T executeWithLock(String userLockName, int timeoutSeconds, Supplier<T> supplier) {
        try {
            getLock(userLockName, timeoutSeconds);
            return supplier.get();
        } finally {
            releaseLock(userLockName);
        }
    }

    private void getLock(String userLockName, int timeoutSeconds) {
        Map<String, Object> params = Map.of(
                "userLockName", userLockName,
                "timeoutSeconds", timeoutSeconds
        );

        Integer result = namedParameterJdbcTemplate.queryForObject(GET_LOCK, params, Integer.class);
        checkResult(result);
    }

    private void releaseLock(String userLockName) {
        Map<String, Object> params = Map.of("userLockName", userLockName);

        Integer result = namedParameterJdbcTemplate.queryForObject(RELEASE_LOCK, params, Integer.class);
        checkResult(result);
    }

    private void checkResult(Integer result) {
        if (result == null || result != 1) {
            throw new BusinessException(ErrorInfo.GENERAL_ERROR);
        }
    }
}