package com.project.usedplatform.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index"; // src/main/resources/templates/index.html 파일을 반환합니다.
    }
}