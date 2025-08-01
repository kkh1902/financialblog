package com.financeblog.service;

import com.financeblog.dto.CommentRequest;
import com.financeblog.entity.Comment;
import com.financeblog.entity.Post;
import com.financeblog.repository.CommentRepository;
import com.financeblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    
    public List<Comment> getApprovedCommentsByPost(Post post) {
        return commentRepository.findByPostAndApprovedTrueOrderByCreatedAtDesc(post);
    }
    
    @Transactional
    public Comment createComment(CommentRequest request) {
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        
        Comment comment = Comment.builder()
                .content(request.getContent())
                .authorName(request.getAuthorName())
                .authorEmail(request.getAuthorEmail())
                .post(post)
                .approved(true) // Auto-approve for now
                .build();
                
        return commentRepository.save(comment);
    }
    
    @Transactional
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        commentRepository.delete(comment);
    }
    
    @Transactional
    public void approveComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        comment.setApproved(true);
        commentRepository.save(comment);
    }
    
    public Page<Comment> getUnapprovedComments(Pageable pageable) {
        return commentRepository.findByApprovedFalse(pageable);
    }
    
    public Long getUnapprovedCommentCount() {
        return commentRepository.countByApprovedFalse();
    }
}