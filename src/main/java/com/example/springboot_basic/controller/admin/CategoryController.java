package com.example.springboot_basic.controller.admin;

import com.example.springboot_basic.model.CategoryDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/categories")
public class CategoryController{
    @GetMapping("add")
    public String add(Model model){
        model.addAttribute("category", new CategoryDto());
        return "admin/categories/addOrEdit";
    }

}
