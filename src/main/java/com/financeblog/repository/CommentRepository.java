package com.financeblog.repository;

import com.financeblog.entity.Comment;
import com.financeblog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    List<Comment> findByPostAndApprovedTrueOrderByCreatedAtDesc(Post post);
    
    Page<Comment> findByApprovedFalse(Pageable pageable);
    
    Long countByApprovedFalse();
    
    Long countByPost(Post post);
}