package doit.shop.domain.user.controller;

import doit.shop.domain.user.dto.UserInfoResponse;
import doit.shop.domain.user.dto.UserLoginRequest;
import doit.shop.domain.user.dto.UserLoginResponse;
import doit.shop.domain.user.dto.UserSignUpRequest;
import doit.shop.domain.user.service.UserService;
import doit.shop.global.auth.dto.TokenDto;
import doit.shop.global.auth.dto.TokenRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController implements UserControllerDocs {

    private final UserService userService;

    @Override
    public void checkDuplicateId(String id) {

    }

    // 회원가입
    @PostMapping
    public ResponseEntity<UserLoginResponse> signUp(@RequestBody UserSignUpRequest userSignUpRequest) {
        return ResponseEntity.ok(userService.signup(userSignUpRequest));
    }

//    // 아이디 검증
//    @PostMapping("/validate")
//    public void checkDuplicateId(@RequestParam String userLoginid) {
//        userService.validateLoginId(userLoginid);
//    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UserLoginRequest userLoginRequest) {
        log.info("Login request: {}", userLoginRequest);
        TokenDto tokenInfo = userService.login(userLoginRequest);
        return ResponseEntity.ok(tokenInfo); // 로그인 성공 시, 토큰 반환
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(userService.reissue(tokenRequestDto));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // 로그아웃 구현 (보통 JWT 토큰은 서버에 저장되지 않으므로 클라이언트가 토큰을 삭제하는 방식으로 처리)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 본인 프로필 불러오기
    @GetMapping("/info")
    public UserInfoResponse getUserInfo(@PathVariable Long userId) {
        // 본인 프로필을 가져오는 로직 (로그인 된 사용자만 접근 가능)
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

//    // 다른 프로필 확인하기
//    @GetMapping("/info/{userId}")
//    public UserInfoResponse getUserInfoById(@PathVariable Long userId) {
//        return userService.findUsers(userId);
//    }
}
