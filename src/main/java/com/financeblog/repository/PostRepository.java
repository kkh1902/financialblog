package com.financeblog.repository;

import com.financeblog.entity.Category;
import com.financeblog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    Optional<Post> findBySlug(String slug);
    
    Page<Post> findByPublishedTrue(Pageable pageable);
    
    Page<Post> findByCategoryAndPublishedTrue(Category category, Pageable pageable);
    
    Page<Post> findByFeaturedTrueAndPublishedTrue(Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.published = true AND " +
           "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Post> searchPosts(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.published = true ORDER BY p.createdAt DESC")
    List<Post> findRecentPosts(Pageable pageable);
    
    @Modifying
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :id")
    void incrementViewCount(@Param("id") Long id);
    
    Long countByPublishedTrue();
    
    Long countByCategoryAndPublishedTrue(Category category);
}