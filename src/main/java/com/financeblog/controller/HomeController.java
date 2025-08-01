package com.financeblog.controller;

import com.financeblog.service.CategoryService;
import com.financeblog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    
    private final PostService postService;
    private final CategoryService categoryService;
    
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("featuredPosts", postService.getFeaturedPosts(
                PageRequest.of(0, 3, Sort.by("createdAt").descending())).getContent());
        model.addAttribute("recentPosts", postService.getRecentPosts(5));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "home/index";
    }
    
    @GetMapping("/about")
    public String about() {
        return "home/about";
    }
}