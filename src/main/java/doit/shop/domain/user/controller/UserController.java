package doit.shop.domain.user.controller;

import doit.shop.domain.user.controller.UserControllerDocs;
import doit.shop.domain.user.dto.UserInfoResponse;
import doit.shop.domain.user.dto.UserLoginRequest;
import doit.shop.domain.user.dto.UserLoginResponse;
import doit.shop.domain.user.dto.UserSignUpRequest;
import doit.shop.domain.user.service.UserService;
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

    private UserService userService;

    @PostMapping("")
    public void signUp(@RequestBody UserSignUpRequest userSignUpRequest) {
    }

    @PostMapping("/validate?id={userLoginid}")
    public void checkDuplicateId(@RequestParam String id) {
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
