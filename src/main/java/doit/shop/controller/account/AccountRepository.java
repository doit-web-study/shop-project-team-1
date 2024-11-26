package doit.shop.controller.account;

import doit.shop.controller.account.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity,Long> {

    AccountEntity findByAccountId(Long accountId);
}
