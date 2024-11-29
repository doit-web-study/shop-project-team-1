package doit.shop.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_ID(409001, "유효하지 않은 아이디 형식입니다. 영문자와 숫자만 사용해주세요.", HttpStatus.CONFLICT),
    DUPLICATE_ID(409002, "이미 존재하는 아이디 입니다.", HttpStatus.CONFLICT),
    INVALID_PASSWORD(409003, "유효하지 않은 비밀번호 형식입니다. 영문자와 숫자만 사용해주세요.", HttpStatus.CONFLICT),
    INVALID_WITHDRAW(409004, "잔액이 부족합니다.", HttpStatus.CONFLICT),
    ERROR(400001, "에러입니다.", HttpStatus.BAD_REQUEST),
    NO_TOKEN_EXIST(400002, "토큰이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(404002, "해당하는 유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ACCOUNT_NOT_FOUND(404002, "해당하는 계좌를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    EXPIRED_TOKEN(401001, "토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED);

    private final int code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(int code, String message, HttpStatus status){
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
