package doit.shop.controller.user;

import doit.shop.controller.user.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@ResponseBody
public class UserController implements UserControllerDocs {

    private final UserService userService;

    @PostMapping("/validate")
    public void checkDuplicateId(@RequestParam String id) {

        userService.checkDuplicateId(id);
    }

    @PostMapping("/signup")
    public void signUp(@Valid @RequestBody UserSignUpRequest userSignUpRequest) {
        userService.signUp(userSignUpRequest);
    }


    @PostMapping("/login")
    public UserLoginResponse login(@RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response) {

        return userService.login(userLoginRequest, response);
    }

    @GetMapping("/{userId}")
    public UserInfoResponse getUserInfo(@PathVariable Long userId) {

        return userService.getUserInfo(userId);
    }

    @PutMapping("/info")
    public void modifyInfo(@RequestBody UserModifyRequest userModifyRequest, HttpServletRequest request){
        userService.modifyInfo(userModifyRequest, request);
    }

    @DeleteMapping("/info")
    public void deleteUser(HttpServletRequest request){
        userService.deleteUser(request);
    }
}
