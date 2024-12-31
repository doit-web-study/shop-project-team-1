package doit.shop.global.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateUserIdException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateUserIdException(DuplicateUserIdException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("detailStatusCode", ex.getDetailStatusCode());
        errorResponse.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

}
