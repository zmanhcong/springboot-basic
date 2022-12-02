package com.example.springboot_basic.controller.admin;

import com.example.springboot_basic.domain.Category;
import com.example.springboot_basic.domain.Product;
import com.example.springboot_basic.model.CategoryDto;
import com.example.springboot_basic.model.ProductDto;
import com.example.springboot_basic.service.CategoryService;
import com.example.springboot_basic.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("admin/products")
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;

    @ModelAttribute("categories")  //share "categories" sang VIEW. dung drop-down cua Category o Product
    public List<CategoryDto> getCategories(){
        return categoryService.findAll().stream().map(item->{  //chuyển thành stream, rồi chuyển thành map để map mỗi phần tử nhận được thành CategoryDto
            CategoryDto dto = new CategoryDto();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).toList();
    }

    @GetMapping("add")
    public String add(Model model){
        model.addAttribute("product", new ProductDto());
        return "admin/products/addOrEdit";
    }

    @PostMapping("saveOrUpdate")
    public ModelAndView saveOrUpdate(ModelMap modelMap,
                                     @ModelAttribute("product") ProductDto productDto_view){
        Product pr_entity = new Product();
        BeanUtils.copyProperties(productDto_view, pr_entity);

        //từ view, lấy được ID của category(dropdown), set id đó cho product.categoryID
        Category category_db = new Category();
        category_db.setCategoryId(productDto_view.getCategoryId());
        pr_entity.setCategory(category_db);

        productService.save(pr_entity);
        modelMap.addAttribute("message", "Product is saved!!");

        return new ModelAndView("forward:/admin/products/list", modelMap);
    }

}
