package com.migaja.service;

import com.migaja.dto.CommentRequest;
import com.migaja.dto.CommentResponse;
import com.migaja.model.Comment;
import com.migaja.model.Story;
import com.migaja.model.User;
import com.migaja.repository.CommentRepository;
import com.migaja.repository.StoryRepository;
import com.migaja.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final StoryRepository storyRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public CommentResponse addComment(Long storyId, CommentRequest request) {
        User currentUser = getCurrentUser();
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new RuntimeException("Historia no encontrada"));
        
        Comment comment = Comment.builder()
                .user(currentUser)
                .story(story)
                .content(request.getContent())
                .build();
        
        comment = commentRepository.save(comment);
        return convertToResponse(comment);
    }
    
    @Transactional(readOnly = true)
    public Page<CommentResponse> getStoryComments(Long storyId, Pageable pageable) {
        return commentRepository.findByStoryIdOrderByCreatedAtDesc(storyId, pageable)
                .map(this::convertToResponse);
    }
    
    @Transactional
    public void deleteComment(Long commentId) {
        User currentUser = getCurrentUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
        
        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("No tienes permiso para eliminar este comentario");
        }
        
        commentRepository.delete(comment);
    }
    
    private CommentResponse convertToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .user(CommentResponse.UserDTO.builder()
                        .id(comment.getUser().getId())
                        .username(comment.getUser().getUsername())
                        .fullName(comment.getUser().getFullName())
                        .avatarUrl(comment.getUser().getAvatarUrl())
                        .build())
                .createdAt(comment.getCreatedAt())
                .build();
    }
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
