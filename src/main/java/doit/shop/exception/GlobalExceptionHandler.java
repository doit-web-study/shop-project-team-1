package doit.shop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, Object>> handleCustomException(CustomException e){
        Map<String, Object> response = new HashMap<>();
        response.put("detailStatusCode", e.getErrorCode());
        response.put("message", e.getErrorMessage());

        return new ResponseEntity<>(response, e.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for(FieldError fieldError : fieldErrors){
            String field = fieldError.getField();
            String message = fieldError.getDefaultMessage();

            //id자릿수
            System.out.println("field : "+field + message);
            if("userLoginId".equals(field) && message.contains("유효")) {
                CustomException customException = new CustomException(ErrorCode.INVALID_ID);
                return handleCustomException(customException);
            } else if ("userPassword".equals(field) && message.contains("유효")) {
                CustomException customException = new CustomException(ErrorCode.INVALID_PASSWORD);
                return handleCustomException(customException);
            }
            else {
                CustomException customException = new CustomException(ErrorCode.ERROR);
                return handleCustomException(customException);
            }
        }
        return null;
    }

}
