package doit.shop.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;

    public CustomException(ErrorCode error) {
        super(error.getMessage());
        this.errorCode = error;
    }

    public int getErrorCode() {
        return errorCode.getCode();
    }
    public String getErrorMessage(){
        return errorCode.getMessage();
    }
    public HttpStatus getHttpStatus(){
        return errorCode.getStatus();
    }
}
