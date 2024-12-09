package doit.shop.controller.account;

import doit.shop.controller.ListWrapper;
import doit.shop.controller.account.dto.AccountIdResponse;
import doit.shop.controller.account.dto.AccountInfoResponse;
import doit.shop.controller.account.dto.AccountRegisterRequest;
import doit.shop.controller.account.dto.AccountUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController implements AccountControllerDocs {

    private final AccountService accountService;

    @PostMapping
    public AccountIdResponse registerAccount(@RequestBody AccountRegisterRequest request) {

        return accountService.registerAccount(request);
    }

    @GetMapping
    public ListWrapper<AccountInfoResponse> getAccountList(HttpServletRequest httpRequest) {

        return accountService.getAccountList(httpRequest);
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
    public void depositAccount(@PathVariable Long accountId, @RequestParam Integer amount) throws InterruptedException {
        accountService.depositAccount(accountId,amount);
    }

    @PostMapping("/{accountId}/withdraw")
    public void withdrawAccount(@PathVariable Long accountId, @RequestParam Integer amount) {
        accountService.withdrawAccount(accountId,amount);
    }
}
