package doit.shop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

// REST API 컨트롤러로 선언
@RestController
@RequestMapping("/api") // API 기본 경로 설정
public class HelloController {

    // GET 요청 처리
    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/json")
    public Map<String, String> json() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to shopping Mall");
        response.put("status", "success");
        return response;
    }

    /**
     * 요청 시 항상 404 에러를 반환
     * URL: /api/error-test
     */
    @GetMapping("/error-test")
    public String errorTest() {
        throw new RuntimeException("This is a test error!");
    }
}
