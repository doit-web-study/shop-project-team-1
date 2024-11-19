package doit.shop.domain.user.dto;

import doit.shop.domain.user.entity.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record UserLoginResponse(
        @Schema(description = "유저 식별 ID", example = "1")
        Long userId,

        @Schema(description = "유저 이름", example = "강수빈")
        String userName
) {

        public static UserLoginResponse from(Users users) {
                return UserLoginResponse.builder()
                        .userId(users.getId())
                        .userName(users.getUsername())
                        .build();
        }
}
