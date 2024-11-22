package doit.shop.controller;

// 사용자 정의 예외 클래스
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
