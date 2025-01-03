package doit.shop.controller.account;

import doit.shop.controller.ListWrapper;
import doit.shop.controller.account.dto.*;
import org.springframework.web.bind.annotation.*;
import doit.shop.controller.account.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController implements AccountControllerDocs {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public AccountIdResponse registerAccount(@RequestBody AccountRegisterRequest request) {
        return accountService.registerAccount(request);
    }

    @GetMapping
    public ListWrapper<AccountInfoResponse> getAccountList() {
        return new ListWrapper<>(accountService.getAccountList());
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
    public void depositAccount(@PathVariable Long accountId, @RequestParam Integer amount) {
        accountService.depositAccount(accountId, amount);
    }

    @PostMapping("/{accountId}/withdraw")
    public void withdrawAccount(@PathVariable Long accountId, @RequestParam Integer amount) {
        accountService.withdrawAccount(accountId, amount);
    }
}
