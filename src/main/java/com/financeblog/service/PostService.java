package com.financeblog.service;

import com.financeblog.dto.PostCreateRequest;
import com.financeblog.dto.PostUpdateRequest;
import com.financeblog.entity.Category;
import com.financeblog.entity.Post;
import com.financeblog.entity.Tag;
import com.financeblog.entity.User;
import com.financeblog.repository.CategoryRepository;
import com.financeblog.repository.PostRepository;
import com.financeblog.repository.TagRepository;
import com.financeblog.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    
    public Page<Post> getPublishedPosts(Pageable pageable) {
        return postRepository.findByPublishedTrue(pageable);
    }
    
    public Page<Post> getPostsByCategory(String categorySlug, Pageable pageable) {
        Category category = categoryRepository.findBySlug(categorySlug)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        return postRepository.findByCategoryAndPublishedTrue(category, pageable);
    }
    
    public Page<Post> getFeaturedPosts(Pageable pageable) {
        return postRepository.findByFeaturedTrueAndPublishedTrue(pageable);
    }
    
    public List<Post> getRecentPosts(int limit) {
        return postRepository.findRecentPosts(PageRequest.of(0, limit));
    }
    
    public Page<Post> searchPosts(String keyword, Pageable pageable) {
        return postRepository.searchPosts(keyword, pageable);
    }
    
    public Post getPostBySlug(String slug) {
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        
        if (!post.getPublished()) {
            throw new IllegalArgumentException("Post is not published");
        }
        
        return post;
    }
    
    @Transactional
    public void incrementViewCount(Long postId) {
        postRepository.incrementViewCount(postId);
    }
    
    @Transactional
    public Post createPost(PostCreateRequest request, User author) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        
        String slug = SlugUtil.generateSlug(request.getTitle());
        
        Post post = Post.builder()
                .title(request.getTitle())
                .slug(slug)
                .content(request.getContent())
                .summary(request.getSummary())
                .author(author)
                .category(category)
                .published(request.getPublished())
                .featured(request.getFeatured())
                .build();
        
        if (request.getTagNames() != null && !request.getTagNames().isEmpty()) {
            List<Tag> tags = processTags(request.getTagNames());
            post.setTags(tags);
        }
        
        Post savedPost = postRepository.save(post);
        
        // Update category post count
        category.setPostCount(category.getPostCount() + 1);
        categoryRepository.save(category);
        
        return savedPost;
    }
    
    @Transactional
    public Post updatePost(Long id, PostUpdateRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        
        if (request.getCategoryId() != null && !request.getCategoryId().equals(post.getCategory().getId())) {
            // Update old category count
            Category oldCategory = post.getCategory();
            oldCategory.setPostCount(Math.max(0, oldCategory.getPostCount() - 1));
            categoryRepository.save(oldCategory);
            
            // Set new category
            Category newCategory = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));
            post.setCategory(newCategory);
            newCategory.setPostCount(newCategory.getPostCount() + 1);
            categoryRepository.save(newCategory);
        }
        
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setSummary(request.getSummary());
        post.setPublished(request.getPublished());
        post.setFeatured(request.getFeatured());
        
        if (request.getTagNames() != null) {
            List<Tag> tags = processTags(request.getTagNames());
            post.setTags(tags);
        }
        
        return postRepository.save(post);
    }
    
    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        
        // Update category count
        Category category = post.getCategory();
        category.setPostCount(Math.max(0, category.getPostCount() - 1));
        categoryRepository.save(category);
        
        postRepository.delete(post);
    }
    
    private List<Tag> processTags(List<String> tagNames) {
        List<Tag> tags = new ArrayList<>();
        
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> {
                        Tag newTag = Tag.builder()
                                .name(tagName)
                                .build();
                        return tagRepository.save(newTag);
                    });
            tags.add(tag);
        }
        
        return tags;
    }
    
    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }
    
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }
    
    public Long getTotalPostCount() {
        return postRepository.countByPublishedTrue();
    }
}