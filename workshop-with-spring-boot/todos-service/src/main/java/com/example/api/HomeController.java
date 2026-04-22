package com.example.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({ "/", "/index" })
    public String index(Model model) {
        model.addAttribute("appName", "Todos Service");
        model.addAttribute("swaggerPath", "/swagger-ui.html");
        return "index";
    }
}