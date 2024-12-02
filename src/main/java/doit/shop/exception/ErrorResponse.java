package doit.shop.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "오류 응답")
public record ErrorResponse(
        @Schema(description = "에러 상태 코드", example = "409001")
        int detailStatusCode,

        @Schema(description = "에러 메시지", example = "유효하지 않은 아이디 형식입니다.")
        String message
) {
    public static ErrorResponse of(int detailStatusCode, String message) {
        return new ErrorResponse(detailStatusCode, message);
    }
}