package com.financeblog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateRequest {
    
    @NotBlank(message = "Title is required")
    @Size(max = 300, message = "Title must not exceed 300 characters")
    private String title;
    
    @NotBlank(message = "Content is required")
    private String content;
    
    @Size(max = 500, message = "Summary must not exceed 500 characters")
    private String summary;
    
    @NotNull(message = "Category is required")
    private Long categoryId;
    
    private List<String> tagNames;
    
    @Builder.Default
    private Boolean published = false;
    
    @Builder.Default
    private Boolean featured = false;
}