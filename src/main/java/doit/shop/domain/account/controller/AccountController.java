package doit.shop.domain.account.controller;

import doit.shop.domain.account.dto.AccountIdResponse;
import doit.shop.domain.account.dto.AccountInfoResponse;
import doit.shop.domain.account.dto.AccountRegisterRequest;
import doit.shop.domain.account.dto.AccountUpdateRequest;
import doit.shop.domain.account.service.AccountService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController implements AccountControllerDocs {

    private final AccountService accountService;

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
    public ResponseEntity<Map<String, String>> depositAccount(@PathVariable Long accountId, @RequestParam Integer amount) {
        try {
            accountService.deposit(accountId, amount);
            return ResponseEntity.ok().build();
        } catch (AccountNotFoundException ex) {
            Map<String, String> response = new HashMap<>();
            response.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/{accountId}/withdraw")
    public void withdrawAccount(@PathVariable Long accountId, @RequestParam Integer amount) {

    }
}
