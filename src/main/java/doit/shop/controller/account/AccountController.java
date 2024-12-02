package doit.shop.controller.account;

import doit.shop.controller.ListWrapper;
import doit.shop.controller.account.dto.AccountIdResponse;
import doit.shop.controller.account.dto.AccountInfoResponse;
import doit.shop.controller.account.dto.AccountRegisterRequest;
import doit.shop.controller.account.dto.AccountUpdateRequest;
import doit.shop.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController implements AccountControllerDocs {

    private final AccountService accountService;

    @PostMapping
    public AccountIdResponse registerAccount(@RequestBody AccountRegisterRequest request) {
        AccountInfoResponse response = accountService.registerAccount(request);
        return new AccountIdResponse(response.accountId());
    }

    @GetMapping
    public ListWrapper<AccountInfoResponse> getAccountList() {
        List<AccountInfoResponse> accounts = accountService.getAccountList();
        return new ListWrapper<>(accounts);
    }

    @GetMapping("/{accountId}")
    public AccountInfoResponse getAccountInfo(@PathVariable Long accountId) {
        return accountService.getAccountInfo(accountId);
    }

    @PutMapping("/{accountId}")
    public AccountInfoResponse updateAccountInfo(@PathVariable Long accountId,
                                                 @RequestBody AccountUpdateRequest request) {
        return accountService.updateAccountInfo(accountId, request);
    }

    @PostMapping("/{accountId}/deposit")
    public ListWrapper<String> depositAccount(@PathVariable Long accountId, @RequestParam Integer amount) {
        accountService.depositAccount(accountId, amount);
        String message = String.format("%d 만큼 계좌 증가", amount);
        return new ListWrapper<>(List.of(message));
    }

    @PostMapping("/{accountId}/withdraw")
    public ListWrapper<String> withdrawAccount(@PathVariable Long accountId, @RequestParam Integer amount) {
        accountService.withdrawAccount(accountId, amount);
        String message = String.format("%d 만큼 계좌 감소", amount);
        return new ListWrapper<>(List.of(message));
    }
}
