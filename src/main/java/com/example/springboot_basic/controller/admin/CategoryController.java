package com.example.springboot_basic.controller.admin;

import com.example.springboot_basic.domain.Category;
import com.example.springboot_basic.model.CategoryDto;
import com.example.springboot_basic.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("admin/categories")
public class CategoryController{
    @Autowired
    CategoryService categoryService;
    @GetMapping("add")
    public String add(Model model){
        model.addAttribute("category", new CategoryDto());
        return "admin/categories/addOrEdit";
    }

    @PostMapping("saveOrUpdate")
    public ModelAndView saveOrUpdate(ModelMap modelMap, CategoryDto categoryDto){
        Category entity = new Category();
        BeanUtils.copyProperties(categoryDto, entity);
        categoryService.save(entity);
        modelMap.addAttribute("message", "Category is saved!!");
        System.out.println("add susscessfully!!!");

        return new ModelAndView("forward:/admin/categories", modelMap);
    }

    @RequestMapping("")
    public String list(ModelMap modelMap){
        Iterable<Category>list = categoryService.findAll();
        modelMap.addAttribute("categories", list);
        return "admin/categories/category-list";
    }

}
