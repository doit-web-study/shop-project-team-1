package doit.shop.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

public record UserLoginRequest(
        @Schema(description = "아이디", example = "testId")
        String userLoginId,

        @Schema(description = "비밀번호", example = "testPassword")
        String userPassword
) {
}