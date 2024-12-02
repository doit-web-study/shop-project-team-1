package doit.shop.exception;

import org.springframework.http.HttpStatus;

public interface ErrorInfoInterface {
    HttpStatus getStatus();
    String getMessage();
    int getDetailStatusCode();
}
