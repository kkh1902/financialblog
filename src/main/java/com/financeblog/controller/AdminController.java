package com.financeblog.controller;

import com.financeblog.dto.PostCreateRequest;
import com.financeblog.dto.PostUpdateRequest;
import com.financeblog.entity.Post;
import com.financeblog.entity.User;
import com.financeblog.service.CategoryService;
import com.financeblog.service.CommentService;
import com.financeblog.service.PostService;
import com.financeblog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final PostService postService;
    private final CategoryService categoryService;
    private final CommentService commentService;
    private final UserService userService;
    
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("totalPosts", postService.getTotalPostCount());
        model.addAttribute("totalCategories", categoryService.getAllCategories().size());
        model.addAttribute("pendingComments", commentService.getUnapprovedCommentCount());
        model.addAttribute("recentPosts", postService.getRecentPosts(5));
        return "admin/dashboard";
    }
    
    @GetMapping("/posts")
    public String managePosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model) {
        
        model.addAttribute("posts", postService.getAllPosts(
                PageRequest.of(page, size, Sort.by("createdAt").descending())));
        return "admin/post-list";
    }
    
    @GetMapping("/posts/new")
    public String newPostForm(Model model) {
        model.addAttribute("postRequest", new PostCreateRequest());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/post-form";
    }
    
    @PostMapping("/posts")
    public String createPost(
            @Valid @ModelAttribute("postRequest") PostCreateRequest request,
            BindingResult result,
            @AuthenticationPrincipal User user,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/post-form";
        }
        
        postService.createPost(request, user);
        redirectAttributes.addFlashAttribute("message", "글이 작성되었습니다.");
        return "redirect:/admin/posts";
    }
    
    @GetMapping("/posts/{id}/edit")
    public String editPostForm(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        
        PostUpdateRequest request = PostUpdateRequest.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .summary(post.getSummary())
                .categoryId(post.getCategory().getId())
                .published(post.getPublished())
                .featured(post.getFeatured())
                .build();
        
        model.addAttribute("postRequest", request);
        model.addAttribute("postId", id);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/post-form";
    }
    
    @PostMapping("/posts/{id}")
    public String updatePost(
            @PathVariable Long id,
            @Valid @ModelAttribute("postRequest") PostUpdateRequest request,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("postId", id);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/post-form";
        }
        
        postService.updatePost(id, request);
        redirectAttributes.addFlashAttribute("message", "글이 수정되었습니다.");
        return "redirect:/admin/posts";
    }
    
    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        postService.deletePost(id);
        redirectAttributes.addFlashAttribute("message", "글이 삭제되었습니다.");
        return "redirect:/admin/posts";
    }
    
    @GetMapping("/categories")
    public String manageCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/category-list";
    }
    
    @PostMapping("/categories")
    public String createCategory(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            RedirectAttributes redirectAttributes) {
        
        try {
            categoryService.createCategory(name, description);
            redirectAttributes.addFlashAttribute("message", "카테고리가 생성되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/admin/categories";
    }
}