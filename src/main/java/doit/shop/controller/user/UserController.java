package doit.shop.controller.user;

import doit.shop.controller.user.dto.*;
import doit.shop.port.jwt.JwtResDto;
import doit.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {

    private final UserService userService;

    @PostMapping
    @Override
    public ResponseEntity<?> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        userService.registerUser(userRegisterRequest.toUser());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/validate")
    @Override
    public ResponseEntity<?> checkDuplicateId(@RequestParam String id) {
        userService.validateUserId(id);
        userService.checkUserIdAvailability(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        JwtResDto jwtResDto = userService.login(userLoginRequest.userLoginId(), userLoginRequest.userPassword());
        return ResponseEntity.ok(new UserLoginResponse(jwtResDto.getAccessToken(), jwtResDto.getRefreshToken()));
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> getMyProfile() {
        String username = getCurrentUsername();
        UserInfoResponse userInfo = userService.getUserInfoByUsername(username);
        return ResponseEntity.ok(userInfo);
    }

    @PutMapping("/info")
    public ResponseEntity<?> updateMyProfile(@RequestBody UserUpdateRequest updateRequest) {
        String username = getCurrentUsername();
        userService.updateUserProfileByUsername(username, updateRequest.userNickname(), updateRequest.userPhoneNumber());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/info")
    public ResponseEntity<?> deleteMyProfile() {
        String username = getCurrentUsername();
        userService.deleteUserByUsername(username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<UserLoginResponse> refreshAccessToken(@RequestBody UserRefreshTokenRequest refreshTokenRequest) {
        String newAccessToken = userService.refreshAccessToken(refreshTokenRequest.RefreshToken());
        return ResponseEntity.ok(new UserLoginResponse(newAccessToken, null));
    }

    // 현재 인증된 사용자의 username을 가져오는 유틸 메서드
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
