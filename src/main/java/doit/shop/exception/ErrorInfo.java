package doit.shop.exception;

import org.springframework.http.HttpStatus;

public enum ErrorInfo implements ErrorInfoInterface {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저 없음", 404002),
    ACCOUNT_NOT_FOUNT(HttpStatus.NOT_FOUND, "계좌가 없음", 404003),
    SON_PARSE_ERROR(HttpStatus.BAD_REQUEST, "JSON 형식 이상", 400003),
    PAYMENT_ERROR(HttpStatus.BAD_REQUEST, "계산해봤는데 이상함", 400004),
    INPUT_ERROR(HttpStatus.BAD_REQUEST, "잘못된 값을 넣은 듯", 400005),
    SECURITY_ERROR(HttpStatus.UNAUTHORIZED, "보안적 이슈", 401001),
    AUTH_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "권한 없음요", 500001),
    CONCURRENCY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "최대 재시도 횟수를 초과", 500002),
    LOCK_ACQUISITION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"락을 얻는데 실패", 500003),
    GENERAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예기치 못한 exception 입니다. 로그를 확인해주세요", 500001);

    private final HttpStatus status;
    private final String message;
    private final int detailStatusCode;

    ErrorInfo(HttpStatus status, String message, int detailStatusCode) {
        this.status = status;
        this.message = message;
        this.detailStatusCode = detailStatusCode;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getDetailStatusCode() {
        return detailStatusCode;
    }
}
