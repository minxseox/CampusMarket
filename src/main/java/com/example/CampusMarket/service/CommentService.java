package com.example.CampusMarket.service;

import com.example.CampusMarket.entity.Comment;
import com.example.CampusMarket.entity.Product;
import com.example.CampusMarket.entity.SiteUser;
import com.example.CampusMarket.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ProductService productService;

    @Transactional
    public void save(Long productId, String content, SiteUser author) {
        Product product = productService.findById(productId);
        Comment comment = new Comment(product, content, author);
        commentRepository.save(comment);
    }

    @Transactional
    public void delete(Long commentId, SiteUser loginUser) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if (comment.getAuthor() == null || !comment.getAuthor().getId().equals(loginUser.getId())) {
            throw new IllegalArgumentException("댓글 삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public List<Comment> findByProduct(Product product) {
        return commentRepository.findByProductOrderByIdDesc(product);
    }
}