package doit.shop.controller.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

public record UserSignUpRequest(

        @Pattern(regexp = "^[a-zA-Z0-9]{5,30}$", message = "유효하지 않은 아이디 형식입니다. 영문자와 숫자만 사용해주세요.")
        @Schema(description = "아이디", example = "testId")
        String userLoginId,

        @Pattern(regexp = "^[a-zA-Z0-9]{5,30}$", message = "유효하지 않은 비밀번호 형식입니다. 영문자와 숫자만 사용해주세요.")
        @Schema(description = "비밀번호", example = "testPassword")
        String userPassword,

        @Schema(description = "이름", example = "홍길동")
        String userName,

        @Schema(description = "닉네임", example = "각시탈")
        String userNickName,

        @Schema(description = "전화번호", example = "010-1234-5678")
        String userPhoneNumber
) {
}
