package com.example.springboot_basic.HomeController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class homeController {

    @GetMapping("/")
    public String home1(Model model) {
        model.addAttribute("something", "hello from controller");
        return "admin/categories/search";
    }

}
