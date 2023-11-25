package com.backend.foodproject.controller;

import com.backend.foodproject.entity.Category;
import com.backend.foodproject.entity.Seller;
import com.backend.foodproject.error.ConflictException;
import com.backend.foodproject.error.GlobalException;
import com.backend.foodproject.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/add")
    public String addCategory
            (@AuthenticationPrincipal Seller seller, @RequestBody Category category) throws ConflictException {
        return categoryService.addCategory(seller, category);
    }

    @GetMapping("/get/all")
    public List<Category> getAllCategory() throws GlobalException {
        return categoryService.getAllCategory();
    }

    @GetMapping("/get")
    public Category findCategoryById(@RequestParam Long id) throws GlobalException {
        return categoryService.findCategoryById(id);
    }

    @PutMapping("/update")
    public String updateCategory
            (@RequestParam Long id, @RequestBody Category category, @AuthenticationPrincipal Seller seller) {
        return categoryService.updateCategory(category, id, seller);
    }

    @DeleteMapping("/delete")
    public String deleteCategory
            (@RequestParam Long id, @AuthenticationPrincipal Seller seller) {
        return categoryService.deleteCategory(id, seller);
    }
}
