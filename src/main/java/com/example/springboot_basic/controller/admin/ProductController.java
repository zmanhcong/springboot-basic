package com.example.springboot_basic.controller.admin;

import com.example.springboot_basic.domain.Category;
import com.example.springboot_basic.domain.Product;
import com.example.springboot_basic.model.CategoryDto;
import com.example.springboot_basic.model.ProductDto;
import com.example.springboot_basic.service.CategoryService;
import com.example.springboot_basic.service.ProductService;
import com.example.springboot_basic.service.StorageService;
import org.h2.engine.Mode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("admin/products")
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;

    @Autowired
    StorageService storageService;

    @RequestMapping("")
    public String list(ModelMap modelMap){
        List<Product> list = productService.findAll();
        modelMap.addAttribute("products", list);
        return "/admin/products/product-list";
    }

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
                                     @Valid @ModelAttribute("product") ProductDto productDto_view,
                                     BindingResult result){  //@Valid,BindingResult for validate product
//        if (result.hasErrors()){
//            return new ModelAndView("admin/products/addOrEdit");
//        }

        Product pr_entity = new Product();
        BeanUtils.copyProperties(productDto_view, pr_entity);

        //từ view, lấy được ID của category(dropdown), set id đó cho product.categoryID
        Category category_db = new Category();
        category_db.setCategoryId(productDto_view.getCategoryId());
        pr_entity.setCategory(category_db);

        if (!productDto_view.getImageFile().isEmpty()){ //function for show image, imageFile được gửi từ addOrEdit.html thông qua POST:  multipart/form-data
            UUID uuid = UUID.randomUUID();
            String uuString = uuid.toString();
            pr_entity.setImage(storageService.getStorageFileName(productDto_view.getImageFile(), uuString));  //set tên image file bằng random, rồi lưu vào database
            storageService.store(productDto_view.getImageFile(), pr_entity.getImage()); //lưu file, với tham số đầu là file lấy từ view, tham số thứ 2 là tên file(tên file đã được đặt random)
        }
        productService.save(pr_entity);
        modelMap.addAttribute("message", "Product is saved!!");

        return new ModelAndView("forward:/admin/products/", modelMap);
    }


    //View detail and edit products
    @GetMapping("images/{filename:.+}")      //ta có tên file, mang tên file đó tìm trong folder rồi load for display images in addOrEdit.html khi view detail product.
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename){
        Resource file = (Resource) storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("edit/{productId}")
    public ModelAndView edit(ModelMap modelMap, @PathVariable("productId") Long productId){
        Optional<Product> opt_productDB = productService.findById(productId);
        ProductDto dto_product = new ProductDto();

        if (opt_productDB.isPresent()){
            Product entity_product = opt_productDB.get();
            BeanUtils.copyProperties(entity_product, dto_product);
            dto_product.setIsEdit(true);

            List<Category> categories = categoryService.findAll();
            modelMap.addAttribute("categories", categories);
            modelMap.addAttribute("product", dto_product);
            return new ModelAndView("admin/products/addOrEdit");
        }

        modelMap.addAttribute("message","Product is not exited");
        return new ModelAndView("forward:/admin/products", modelMap);
    }

    @GetMapping("delete/{productId}")
    public ModelAndView delete(ModelMap modelMap,
                               @PathVariable("productId") Long productId) {
        productService.deleteById(productId);
        modelMap.addAttribute("message", "Product is deleted!!!");
        return new ModelAndView("forward:/admin/products/", modelMap);
    }

}
