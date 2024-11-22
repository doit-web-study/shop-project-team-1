package doit.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // 아래 클래스 Spring MVC 컨트롤러임.
// 컨트롤러 : 사용자의 요청을 받아 처리하고 그 결과를 보여줄 뷰 반환.
public class HomeController {

    @GetMapping // 해당 메서드가 url(" / ") 에 대한 get 요청 처리함을 나타냄.
    public String home(Model model) { // home 이라는 이름의 메서드 선언, model 객체를 매개변수로 받는다.
        model.addAttribute("message", "Welcome to the Shopping Mall");
        // model 객체에 message 이름으로 해당 문자열을 추가한다. 이 데이터는 뷰로 전달된다.
        return "home";
        // home 이라는 이름의 뷰 반환.
        // spring MVC는 이 이름을 사용해 적절한 뷰를 찾고 랜더링 함.
        // 'home.html' or 'home.jsp' 같은 파일이 됨.

    }

}
