package com.example.springboot_basic.controller.admin;

import com.example.springboot_basic.domain.Category;
import com.example.springboot_basic.model.CategoryDto;
import com.example.springboot_basic.repository.CategoryRepository;
import com.example.springboot_basic.service.CategoryService;
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
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("admin/categories")
public class CategoryController{
    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

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
        List<Category>list = categoryService.findAll();
        modelMap.addAttribute("categories", list);
        return "/admin/categories/category-list";
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

    @GetMapping("/search")
    public String search(ModelMap modelMap, @RequestParam(name = "name") String name) {
        List<Category> listCategory_db =  categoryService.findAllNative(name);
        modelMap.addAttribute("categories", listCategory_db);
//        return new ModelAndView("forward:/admin/categories/list", modelMap);
        return "/admin/categories/category-list";
    }

    //Pagination
    @GetMapping("searchpaginated")
    public String search(ModelMap model,
                         @RequestParam(name = "name", required = false) String name,
                         @RequestParam("page")Optional<Integer> page,
                         @RequestParam("size")Optional<Integer> size) {
        int currentPage = page.orElse(1);  //if receive value form html, use that value, else use value = 1
        int pageSize = size.orElse(5);

        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by("name"));
        Page<Category> resultPage = null;

        if(StringUtils.hasText(name)) {    //Nếu có imput đầu vào, thì findByName Containing, và trả tra view name đó để hiển thị cho user biết là đang search bằng keyword gì, ếu không có keyword input đầu vào thì sẽ findAll.
            resultPage = categoryService.findByNameContaining(name, pageable);
            model.addAttribute("name", name);  // lấy cái name để view truyền param khi chuyển page. ví dụ: categories/searchpaginated(name = ${name},
        }
        else {
            resultPage = categoryService.findAll(pageable);
        }

        int totalPages = resultPage.getTotalPages() - 1;
        System.out.printf("totalPage is: " + totalPages + "%n" );
        if (totalPages > 0) {
            int start = Math.max(1, currentPage - 2); //ページStartは最低の値は１
            int end = Math.min(currentPage + 2, totalPages);

            List<Integer>pageNumbers = IntStream.rangeClosed(start, end)
                    .boxed()
                    .collect(Collectors.toList());
            System.out.printf("pageNumbers is: " + pageNumbers + "%n" ); //Output sẽ là page number dùng hiển thị ở view, ví dụ: [1, 2, 3]  => First 1 2 3 Last
            model.addAttribute("pageNumbers",pageNumbers);
        }
        model.addAttribute("categoryPage", resultPage);

        return "/admin/categories/searchpaginated";
    }
}
