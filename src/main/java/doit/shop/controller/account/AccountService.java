package doit.shop.controller.account;

import doit.shop.common.jwt.JwtProvider;
import doit.shop.controller.ListWrapper;
import doit.shop.controller.account.dto.AccountIdResponse;
import doit.shop.controller.account.dto.AccountInfoResponse;
import doit.shop.controller.account.dto.AccountRegisterRequest;
import doit.shop.controller.account.dto.AccountUpdateRequest;
import doit.shop.controller.account.entity.AccountEntity;
import doit.shop.controller.user.UserRepository;
import doit.shop.controller.user.entity.UserEntity;
import doit.shop.exception.CustomException;
import doit.shop.exception.ErrorCode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private JwtProvider jwtProvider;
    private UserRepository userRepository;
    private AccountRepository accountRepository;

    @Transactional
    public AccountIdResponse registerAccount(AccountRegisterRequest request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByLoginId(userId);

        AccountEntity account = AccountEntity.builder()
                .accountBankName(request.accountBankName())
                .accountNumber(request.accountNumber())
                .accountName(request.accountName())
                .userId(user.getUserId())
                .build();

        return new AccountIdResponse(account.getAccountId());
    }
    public ListWrapper<AccountInfoResponse> getAccountList() {
        List<AccountEntity> all = accountRepository.findAll();
        List<AccountInfoResponse> responses = all.stream().map(a ->
            new AccountInfoResponse(a.getAccountId(), a.getAccountName(), a.getAccountNumber(), a.getAccountBankName(), a.getAccountBalance())
        ).collect(Collectors.toList());

        return new ListWrapper<>(responses);
    }

    public AccountInfoResponse getAccountInfo(Long accountId) {
        AccountEntity account = accountRepository.findByAccountId(accountId);
        if(account==null){
            throw new CustomException(ErrorCode.ACCOUNT_NOT_FOUND);
        }
        return new AccountInfoResponse(account.getAccountId(),account.getAccountName(),account.getAccountNumber(),account.getAccountBankName(),account.getAccountBalance());
    }

    @Transactional
    public AccountInfoResponse updateAccountInfo(Long accountId, AccountUpdateRequest request) {
        AccountEntity account = accountRepository.findByAccountId(accountId);
        if(account==null){
            throw new CustomException(ErrorCode.ACCOUNT_NOT_FOUND);
        }
        account.updateName(request.accountName());
        accountRepository.save(account);

        return new AccountInfoResponse(account.getAccountId(),account.getAccountName(),account.getAccountNumber(),account.getAccountBankName(),account.getAccountBalance());
    }

    @Transactional
    public void depositAccount(Long accountId, Integer amount) {
        AccountEntity account = accountRepository.findByAccountId(accountId);
        if(account==null){
            throw new CustomException(ErrorCode.ACCOUNT_NOT_FOUND);
        }
        account.deposit(amount);
    }

    @Transactional
    public void withdrawAccount(Long accountId, Integer amount) {
        AccountEntity account = accountRepository.findByAccountId(accountId);
        if(account==null){
            throw new CustomException(ErrorCode.ACCOUNT_NOT_FOUND);
        }
        if(account.getAccountBalance()<amount){
            throw new CustomException(ErrorCode.INVALID_WITHDRAW);
        }
        account.withdraw(amount);
    }
}
