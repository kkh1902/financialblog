package com.financeblog.service;

import com.financeblog.entity.Category;
import com.financeblog.repository.CategoryRepository;
import com.financeblog.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }
    
    public Category getCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }
    
    @Transactional
    public Category createCategory(String name, String description) {
        String slug = SlugUtil.generateSlug(name);
        
        if (categoryRepository.existsBySlug(slug)) {
            throw new IllegalArgumentException("Category with this slug already exists");
        }
        
        Category category = Category.builder()
                .name(name)
                .slug(slug)
                .description(description)
                .build();
                
        return categoryRepository.save(category);
    }
    
    @Transactional
    public Category updateCategory(Long id, String name, String description) {
        Category category = getCategoryById(id);
        
        category.setName(name);
        category.setDescription(description);
        
        return categoryRepository.save(category);
    }
    
    @Transactional
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        
        if (category.getPostCount() > 0) {
            throw new IllegalStateException("Cannot delete category with posts");
        }
        
        categoryRepository.delete(category);
    }
}