package doit.shop.domain.user.controller;

import doit.shop.domain.user.controller.UserControllerDocs;
import doit.shop.domain.user.dto.UserInfoResponse;
import doit.shop.domain.user.dto.UserLoginRequest;
import doit.shop.domain.user.dto.UserLoginResponse;
import doit.shop.domain.user.dto.UserSignUpRequest;
import doit.shop.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController implements UserControllerDocs {
    @Autowired
    private UserService userService;

    // 회원가입
    @PostMapping
    public UserLoginResponse signUp(@RequestBody UserSignUpRequest userSignUpRequest) {
        return userService.signUp(userSignUpRequest);
    }

    // 아이디 검증
    @PostMapping("/validate?id={userLoginid}")
    public void checkDuplicateId(@RequestParam String userLoginid) {
        userService.validateLoginId(userLoginid);
    }

    // 로그인
    @PostMapping("/login")
    public UserLoginResponse login(@RequestBody UserLoginRequest userLoginRequest) {
        return userService.login(userLoginRequest);
    }

    // 로그아웃
    @PostMapping("/logout")
    public UserLoginResponse logout(@RequestBody UserLoginRequest userLoginRequest) {
        return null;
    }

    // 본인 프로필 불러오기
    @GetMapping("/info")
    public UserInfoResponse getUserInfo(@PathVariable Long userId) {
        return null;
    }


    // 본인 프로필 수정하기
    @PutMapping("/info")
    public UserInfoResponse updateUserInfo(@RequestBody UserInfoResponse userInfoResponse) {
        return null;
    }

    // 회원탈퇴
    @DeleteMapping("/info")
    public UserInfoResponse deleteUserInfo(@RequestBody UserInfoResponse userInfoResponse) {
        return null;
    }

    // 다른 프로필 확인하기
    @GetMapping("/info/{userId}")
    public UserInfoResponse getUserInfoById(@PathVariable String userId) {
        return null;
    }
}
