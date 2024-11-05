package doit.shop.controller.user;

import doit.shop.controller.user.dto.UserInfoResponse;
import doit.shop.controller.user.dto.UserLoginRequest;
import doit.shop.controller.user.dto.UserLoginResponse;
import doit.shop.controller.user.dto.UserSignUpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController implements UserControllerDocs {

    @PostMapping("/validate")
    public void checkDuplicateId(@RequestParam String id) {

    }

    @PostMapping("/signup")
    public void signUp(@RequestBody UserSignUpRequest userSignUpRequest) {

    }

    @PostMapping("/login")
    public UserLoginResponse login(@RequestBody UserLoginRequest userLoginRequest) {
        return null;
    }

    @GetMapping("/{userId}")
    public UserInfoResponse getUserInfo(@PathVariable Long userId) {
        return null;
    }
}
