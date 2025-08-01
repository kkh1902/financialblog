package com.financeblog.controller;

import com.financeblog.entity.Post;
import com.financeblog.service.CategoryService;
import com.financeblog.service.CommentService;
import com.financeblog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PostController {
    
    private final PostService postService;
    private final CategoryService categoryService;
    private final CommentService commentService;
    
    @GetMapping("/posts")
    public String listPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        
        Page<Post> posts = postService.getPublishedPosts(
                PageRequest.of(page, size, Sort.by("createdAt").descending()));
        
        model.addAttribute("posts", posts);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "post/list";
    }
    
    @GetMapping("/posts/{slug}")
    public String viewPost(@PathVariable String slug, Model model) {
        Post post = postService.getPostBySlug(slug);
        postService.incrementViewCount(post.getId());
        
        model.addAttribute("post", post);
        model.addAttribute("comments", commentService.getApprovedCommentsByPost(post));
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("recentPosts", postService.getRecentPosts(5));
        return "post/detail";
    }
    
    @GetMapping("/category/{slug}")
    public String categoryPosts(
            @PathVariable String slug,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        
        Page<Post> posts = postService.getPostsByCategory(slug,
                PageRequest.of(page, size, Sort.by("createdAt").descending()));
        
        model.addAttribute("posts", posts);
        model.addAttribute("category", categoryService.getCategoryBySlug(slug));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "post/list";
    }
    
    @GetMapping("/search")
    public String search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        
        Page<Post> posts = postService.searchPosts(q,
                PageRequest.of(page, size, Sort.by("createdAt").descending()));
        
        model.addAttribute("posts", posts);
        model.addAttribute("keyword", q);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "post/search";
    }
}