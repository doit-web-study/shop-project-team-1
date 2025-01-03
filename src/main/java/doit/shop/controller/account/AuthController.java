//package doit.shop.controller.account;
//
//import doit.shop.entity.User;
//import doit.shop.repository.UserRepository;
//import doit.shop.security.JwtTokenProvider;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//
//    private final UserRepository userRepository;
//    private final JwtTokenProvider jwtTokenProvider;
//
//    public AuthController(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
//        this.userRepository = userRepository;
//        this.jwtTokenProvider = jwtTokenProvider;
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody User user) {
//        User foundUser = userRepository.findByUsername(user.getUsername());
//        if (foundUser != null && foundUser.getPassword().equals(user.getPassword())) {
//            String token = jwtTokenProvider.createToken(user.getUsername());
//            return ResponseEntity.ok().body("Bearer " + token); // 클라이언트에 JWT 반환
//        }
//        return ResponseEntity.status(401).body("Invalid username or password");
//    }
//
////    /**
////     * 회원가입
////     * @param user 클라이언트가 전송한 회원 정보
////     * @return 성공 메시지
////     */
////    @PostMapping("/signup")
////    public ResponseEntity<String> signup(@RequestBody User user) {
////        // 이미 존재하는 사용자 확인
////        if (userRepository.findByUsername(user.getUsername()) != null) {
////            return ResponseEntity.badRequest().body("Username already exists");
////        }
////
////        // 새로운 회원 정보 저장
////        userRepository.save(user);
////        return ResponseEntity.ok().body("Successfully signed up");
////    }
//
////    @GetMapping("/signup") // GET 요청에 대한 처리
////    public String signupPage() {
////        return "GET method is not supported for signup. Please use POST.";
////    }
//}
