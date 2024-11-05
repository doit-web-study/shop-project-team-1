package doit.shop.controller.account;

import doit.shop.controller.ListWrapper;
import doit.shop.controller.account.dto.AccountIdResponse;
import doit.shop.controller.account.dto.AccountInfoResponse;
import doit.shop.controller.account.dto.AccountRegisterRequest;
import doit.shop.controller.account.dto.AccountUpdateRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController implements AccountControllerDocs {

    @PostMapping
    public AccountIdResponse registerAccount(@RequestBody AccountRegisterRequest request) {
        return null;
    }

    @GetMapping
    public ListWrapper<AccountInfoResponse> getAccountList() {
        return null;
    }

    @GetMapping("/{accountId}")
    public AccountInfoResponse getAccountInfo(@PathVariable Long accountId) {
        return null;
    }

    @PutMapping("/{accountId}")
    public AccountInfoResponse updateAccountInfo(@PathVariable Long accountId,
                                                 @RequestBody AccountUpdateRequest request) {
        return null;
    }

    @PostMapping("/{accountId}/deposit")
    public void depositAccount(@PathVariable Long accountId, @RequestParam Integer amount) {

    }

    @PostMapping("/{accountId}/withdraw")
    public void withdrawAccount(@PathVariable Long accountId, @RequestParam Integer amount) {

    }
}
