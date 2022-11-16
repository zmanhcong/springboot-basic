package com.example.springboot_basic.controller.admin;

import com.example.springboot_basic.domain.Category;
import com.example.springboot_basic.model.CategoryDto;
import com.example.springboot_basic.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;


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
    public ModelAndView saveOrUpdate(ModelMap modelMap, @Valid @ModelAttribute("category") CategoryDto categoryDto,
                                     BindingResult result){  // validate cho category
        if(result.hasErrors()) {
            return new ModelAndView("admin/categories/addOrEdit");
        }

        Category entity = new Category();
        BeanUtils.copyProperties(categoryDto, entity);
        categoryService.save(entity);
        modelMap.addAttribute("message", "Category is saved!!");
        System.out.println("add susscessfully!!!");

        return new ModelAndView("forward:/admin/categories/list", modelMap);
    }

    @RequestMapping("list")
    public String list(ModelMap modelMap){
        Iterable<Category>list = categoryService.findAll();
        modelMap.addAttribute("categories", list);
        return "admin/categories/category-list";
    }

    //Update category
    @GetMapping("edit/{categoryId}")
    public ModelAndView edit(ModelMap modelMap, @PathVariable("categoryId") Long categoryId){
        Optional<Category> findIdInDB = categoryService.findById(categoryId);
        CategoryDto categoryDto = new CategoryDto();

        if (findIdInDB.isPresent()) {
            Category findIdInDB_object = findIdInDB.get(); //Ở đây phải dùng Category chứ không phả là CategoryDto, vì thực thể đang là category(từ database) , đang dùng get() là để lấy dữ liệu từ Optional -> chuyển sang object
            BeanUtils.copyProperties(findIdInDB_object,categoryDto); //đưa từ entity(database) qua dto để sang view
            categoryDto.setIsEdit(true); //dựa vào trường này mà bật chế độ add or edit ở view

            modelMap.addAttribute("category", categoryDto);
            return new ModelAndView("admin/categories/addOrEdit",modelMap); //sử dụng ModeandView nên phải return ra modeAndView. và ModeAndView sẽ render sang view sử dụng như return thường thui
        }
        modelMap.addAttribute("message", "Category is not exited");
        return new ModelAndView("forward:/admin/categories/list", modelMap);
    }

    @GetMapping("delete/{categoryId}")
    public ModelAndView delete(ModelMap modelMap,
                               @PathVariable("categoryId") Long categoryId) {
        categoryService.deleteById(categoryId);
        modelMap.addAttribute("message", "Category is deleted!!!");
        return new ModelAndView("forward:/admin/categories/list", modelMap);
    }
}
