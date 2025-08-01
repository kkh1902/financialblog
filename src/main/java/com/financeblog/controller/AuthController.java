package com.financeblog.controller;

import com.financeblog.dto.UserRegistrationRequest;
import com.financeblog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    
    @GetMapping("/login")
    public String loginForm() {
        return "auth/login";
    }
    
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registrationRequest", new UserRegistrationRequest());
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("registrationRequest") UserRegistrationRequest request,
            BindingResult result,
            Model model) {
        
        if (result.hasErrors()) {
            return "auth/register";
        }
        
        try {
            userService.registerUser(request);
            return "redirect:/login?registered";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }
}