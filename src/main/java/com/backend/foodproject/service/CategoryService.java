package com.backend.foodproject.service;

import com.backend.foodproject.entity.Category;
import com.backend.foodproject.entity.Seller;
import com.backend.foodproject.error.ConflictException;
import com.backend.foodproject.error.GlobalException;
import com.backend.foodproject.reposiory.CategoryRepository;
import com.backend.foodproject.reposiory.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;

    public String addCategory
            (Seller seller, Category category) throws ConflictException {
        Seller savedSeller = sellerRepository.findByEmail(seller.getEmail());
        Category addedCategory = Category.builder()
                .name(category.getName())
                .build();
        categoryRepository.save(addedCategory);
        return "category successfully saved";
    }

    public List<Category> getAllCategory() throws GlobalException {
        List<Category> categories = categoryRepository.findAll();
        if (!categories.isEmpty()) {
            return categories;
        }
        throw new GlobalException("categories not available");
    }

    public Category findCategoryById(Long id) throws GlobalException {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            return category.get();
        }
        throw new GlobalException("category with id " + id + " not found");
    }

    public String updateCategory(Category category, Long id, Seller seller) {
        sellerRepository.findByEmail(seller.getEmail());
        Category updateCat = categoryRepository.findById(id).get();
        if (Objects.nonNull(category.getName()) && !"".equalsIgnoreCase(category.getName())) {
            updateCat.setName(category.getName());
        }
        categoryRepository.save(updateCat);
        return "category successfully updated";
    }

    public String deleteCategory(Long id, Seller seller) {
        sellerRepository.findByEmail(seller.getEmail());
        Category updateCat = categoryRepository.findById(id).get();
        categoryRepository.deleteById(id);
        return "category deleted successfully";
    }
}
