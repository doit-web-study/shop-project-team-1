package doit.shop.controller.user;

import doit.shop.common.jwt.JwtProvider;
import doit.shop.common.jwt.RefreshTokenRepository;
import doit.shop.common.jwt.RefreshToken;
import doit.shop.controller.user.dto.*;
import doit.shop.controller.user.entity.UserEntity;
import doit.shop.exception.CustomException;
import doit.shop.exception.ErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository tokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;
    public void checkDuplicateId(String LoginId) {
        if(userRepository.existsByloginId(LoginId)){
            throw new CustomException(ErrorCode.DUPLICATE_ID);
        }
    }

    @Transactional
    public void signUp(UserSignUpRequest userSignUpRequest) {
        checkDuplicateId(userSignUpRequest.userLoginId());

        UserEntity userEntity = UserEntity.builder()
                .loginId(userSignUpRequest.userLoginId())
                .password(bCryptPasswordEncoder.encode(userSignUpRequest.userPassword()))
                .name(userSignUpRequest.userName())
                .phoneNumber(userSignUpRequest.userPhoneNumber())
                .nickname(userSignUpRequest.userNickName())
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(userEntity);
    }

    @Transactional
    public UserLoginResponse login(UserLoginRequest userLoginRequest, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userLoginRequest.userLoginId(), userLoginRequest.userPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        String accessToken = jwtProvider.generateAccessToken(authentication);
        String refreshToken = jwtProvider.generateRefreshToken(authentication);

        response.setHeader("Authorization", "Bearer " + accessToken);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);  // 자바스크립트에서 접근 ㄴㄴ
        refreshTokenCookie.setSecure(true);    // HTTPS에서만 전송
        refreshTokenCookie.setPath("/");       // 전체 경로에서 유효
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 7);  // 쿠키 만료 시간 (7일)
        response.addCookie(refreshTokenCookie);

        UserEntity user = (UserEntity) authentication.getPrincipal();
        RefreshToken token = new RefreshToken(user.getLoginId(), refreshToken);
        tokenRepository.save(token);

        return new UserLoginResponse(user.getUserId());
    }


    public UserInfoResponse getUserInfo(Long userId) {

        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserInfoResponse.builder()
                .userId(userId)
                .userLoginId(user.getLoginId())
                .userName(user.getName())
                .userNickName(user.getNickname())
                .userPhoneNumber(user.getPhoneNumber())
                .build();
    }

    public void logout(String token){
        // 사용자 존재한다면
        jwtProvider.disableToken(token);
    }

    @Transactional
    public void modifyInfo(UserModifyRequest userModifyRequest, HttpServletRequest request){
        String accessToken = jwtProvider.resolveToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        if(jwtProvider.isValidToken(accessToken, userId)){
            UserEntity user = userRepository.findByLoginId(userId);
            user.modifyInfo(userModifyRequest.nickname(),userModifyRequest.phoneNumber());
            userRepository.save(user);
        }
    }

    @Transactional
    public void deleteUser(HttpServletRequest request){
        String accessToken = jwtProvider.resolveToken(request);
        String userId = jwtProvider.getUserId(accessToken);

        if(jwtProvider.isValidToken(accessToken, userId)){
            UserEntity user = userRepository.findByLoginId(userId);
            userRepository.delete(user);
            jwtProvider.disableToken(accessToken);
        }
    }
}
