package doit.shop.domain.user.dto;

import doit.shop.domain.user.entity.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.Optional;

@Builder
public record UserInfoResponse(
        @Schema(description = "유저 식별 ID", example = "1")
        Long userId,

        @Schema(description = "로그인 아이디", example = "testId")
        String userLoginId,

        @Schema(description = "이름", example = "홍길동")
        String userName,

        @Schema(description = "닉네임", example = "각시탈")
        String userNickName,

        @Schema(description = "전화번호", example = "010-1234-5678")
        String userPhoneNumber
) {
        public static UserInfoResponse from(Users users) {
                return UserInfoResponse.builder()
                        .userId(users.getId())
                        .userName(users.getUsername())
                        .userNickName(users.getUserNickname())
                        .userPhoneNumber(users.getUserPhone())
                        .build();
        }
}
