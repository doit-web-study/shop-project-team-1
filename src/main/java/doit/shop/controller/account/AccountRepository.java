package doit.shop.controller.account;

import doit.shop.controller.account.entity.AccountEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity,Long> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select a from AccountEntity a where a.accountId = :accountId")
    AccountEntity findByAccountId(Long accountId);
}
