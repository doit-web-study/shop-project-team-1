package doit.shop.domain.user.service;

import doit.shop.domain.user.dto.UserLoginRequest;
import doit.shop.domain.user.dto.UserLoginResponse;
import doit.shop.domain.user.dto.UserSignUpRequest;
import doit.shop.domain.user.entity.Users;
import doit.shop.domain.user.repository.UserRepository;
//import doit.shop.global.auth.dto.TokenInfo;
import doit.shop.global.auth.Authority;
import doit.shop.global.auth.TokenProvider;
import doit.shop.global.auth.dto.TokenDto;
import doit.shop.global.auth.dto.TokenRequestDto;
import doit.shop.global.auth.entity.RefreshToken;
import doit.shop.global.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokenDto login(UserLoginRequest userLoginRequest) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userLoginRequest.userLoginId(), userLoginRequest.userPassword());

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

//        // 인증이 성공하면 authenticated 값을 true로 설정 (일반적으로 자동으로 처리됨)
//        if (authentication != null && !authentication.isAuthenticated()) {
//            ((UsernamePasswordAuthenticationToken) authentication).setAuthenticated(true);
//        }


        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
//        log.info(tokenInfo.toString());

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return tokenDto;

//        // id와 password로 기존 user 조회
//        Users users = userRepository.findByUserLoginIdAndUserPassword(userLoginRequest.userLoginId(), userLoginRequest.userPassword());
//        // 조회 안 될 경우 에러 발생
//        if (users == null) {
//            throw new IllegalArgumentException("일치하는 회원 없음");
//        }
//        log.info("로그인 성공!!");
//        return UserLoginResponse.from(users);
    }

    public UserLoginResponse signup(UserSignUpRequest userSignUpRequest) {
//        if (userRepository.findByUserLoginId(userSignUpRequest.userLoginId()) != null) {
//            throw new DuplicateUserIdException("이미 존재하는 아이디 입니다.", 409002);
//        }

        Users users = Users.builder()
                .username(userSignUpRequest.userName())
                .userLoginId(userSignUpRequest.userLoginId())
                // password 암호화
                .userPassword(passwordEncoder.encode(userSignUpRequest.userPassword()))
                .userNickname(userSignUpRequest.userNickName())
                .userPhone(userSignUpRequest.userPhoneNumber())
                .authority(Authority.ROLE_USER)
                .build();

        Users savedUser = userRepository.save(users);
        return UserLoginResponse.from(savedUser);
    }

    public void validateLoginId(String userLoginid) {
        // 해당 아이디에 대한 데이터가 있으면 가져옴
        Optional<Users> users = userRepository.findByUserLoginId(userLoginid);
        // 데이터가 있으면 중복 처리
        if (users != null) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.RefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.AccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.RefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }

//    public UserInfoResponse findUsers(Long userId) {
//        Users users = userRepository.findByIdOrNull(userId);
//        return UserInfoResponse.from(users);
//    }
}
