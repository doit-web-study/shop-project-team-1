package doit.shop.domain.user.service;

import doit.shop.domain.user.controller.UserController;
import doit.shop.domain.user.dto.UserLoginRequest;
import doit.shop.domain.user.dto.UserLoginResponse;
import doit.shop.domain.user.dto.UserSignUpRequest;
import doit.shop.domain.user.entity.Users;
import doit.shop.domain.user.repository.UserRepository;
import doit.shop.global.common.exception.DuplicateUserIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserLoginResponse login(UserLoginRequest userLoginRequest) {
        // id와 password로 기존 user 조회
        Users users = userRepository.findByUserLoginIdAndUserPassword(userLoginRequest.userLoginId(), userLoginRequest.userPassword());
        // 조회 안 될 경우 에러 발생
        if (users == null) {
            throw new IllegalArgumentException("일치하는 회원 없음");
        }
        log.info("로그인 성공!!");
        return UserLoginResponse.from(users);
    }


    public UserLoginResponse signUp(UserSignUpRequest userSignUpRequest) {
        if (userRepository.findByUserLoginId(userSignUpRequest.userLoginId()) != null) {
            throw new DuplicateUserIdException("이미 존재하는 아이디 입니다.", 409002);
        }

        Users users = Users.builder()
                .userName(userSignUpRequest.userName())
                .userLoginId(userSignUpRequest.userLoginId())
                .userPassword(userSignUpRequest.userPassword())
                .userNickname(userSignUpRequest.userNickName())
                .userPhone(userSignUpRequest.userPhoneNumber())
                .build();

        Users savedUser = userRepository.save(users);
        return UserLoginResponse.from(savedUser);
    }

    public void validateLoginId(String userLoginid) {
        // 해당 아이디에 대한 데이터가 있으면 가져옴
        Users users = userRepository.findByUserLoginId(userLoginid);
        // 데이터가 있으면 중복 처리
        if (users != null) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
    }
}
