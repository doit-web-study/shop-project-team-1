package doit.shop.service;

import doit.shop.controller.user.dto.UserInfoResponse;
import doit.shop.domain.User;
import doit.shop.exception.UserNotFoundException;
import doit.shop.port.jwt.JwtPort;
import doit.shop.port.jwt.JwtResDto;
import doit.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtPort jwtPort;

    private static final Pattern ID_PATTERN = Pattern.compile("^[a-zA-Z0-9]{5,30}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9]{5,30}$");

    public void registerUser(User user) {
        validateUserId(user.getUserLoginId());
        validateUserPassword(user.getUserPassword());
        checkUserIdAvailability(user.getUserLoginId());

        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        userRepository.save(user);
    }

    public boolean isUserIdValid(String userLoginId) {
        return ID_PATTERN.matcher(userLoginId).matches();
    }

    public boolean isPasswordValid(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    public void validateUserId(String userLoginId) {
        if (!isUserIdValid(userLoginId)) {
            throw new IllegalArgumentException("유효하지 않은 아이디 형식입니다. 영문자와 숫자만 사용해주세요.");
        }
    }

    public void validateUserPassword(String password) {
        if (!isPasswordValid(password)) {
            throw new IllegalArgumentException("유효하지 않은 비밀번호 형식입니다. 영문자와 숫자만 사용해주세요.");
        }
    }

    public void checkUserIdAvailability(String userLoginId) {
        if (!isUserIdAvailable(userLoginId)) {
            throw new IllegalArgumentException("이미 존재하는 아이디 입니다.");
        }
    }

    public boolean isUserIdAvailable(String userLoginId) {
        return userRepository.findByUserLoginId(userLoginId).isEmpty();
    }

    public Optional<User> findUserByLoginId(String userLoginId) {
        return userRepository.findByUserLoginId(userLoginId);
    }

    public JwtResDto login(String userLoginId, String password) {
        User user = userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getUserPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        // JWT 토큰 생성
        return jwtPort.generateTokenPair(user.getUserLoginId());
    }

    public String refreshAccessToken(String refreshToken) {
        return jwtPort.refreshAccessToken(refreshToken);
    }

    public UserInfoResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return UserInfoResponse.from(user);
    }

    public UserInfoResponse getUserInfoByUsername(String username) {
        User user = userRepository.findByUserLoginId(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return UserInfoResponse.from(user);
    }

    public void updateUserProfile(Long userId, String userNickname, String userPhoneNumber) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setUserNickname(userNickname);
        user.setUserPhoneNumber(userPhoneNumber);
        userRepository.save(user);
    }

    public void updateUserProfileByUsername(String username, String userNickname, String userPhoneNumber) {
        User user = userRepository.findByUserLoginId(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setUserNickname(userNickname);
        user.setUserPhoneNumber(userPhoneNumber);
        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.deleteById(userId);
    }

    public void deleteUserByUsername(String username) {
        User user = userRepository.findByUserLoginId(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
    }

    public Boolean isExistUserByPhoneNumber(String phoneNumber) {
        return userRepository.existsByUserPhoneNumber(phoneNumber);
    }

    public User findUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByUserPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
