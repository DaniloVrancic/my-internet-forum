package org.etf.unibl.SecureForum.controller;

import jakarta.validation.Valid;
import org.etf.unibl.SecureForum.model.dto.Comment;
import org.etf.unibl.SecureForum.model.entities.CommentEntity;
import org.etf.unibl.SecureForum.model.requests.CreateCommentRequest;
import org.etf.unibl.SecureForum.model.requests.EditCommentRequest;
import org.etf.unibl.SecureForum.model.requests.UpdateCommentRequest;
import org.etf.unibl.SecureForum.repositories.CommentRepository;
import org.etf.unibl.SecureForum.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@Validated
public class CommentController {

    private final CommentRepository commentRepository;
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentRepository commentRepository, CommentService commentService){
        this.commentRepository = commentRepository;
        this.commentService = commentService;
    }

    @GetMapping
    public List<Comment> findAllComments() {
        return commentService.findAllComments();
    }

    @GetMapping("/forum_post/{post_id}")
    public List<Comment> findAllByPostId(@PathVariable("post_id") Integer postId) {
        return commentService.findAllCommentsByForumPostId(postId);
    }

    @GetMapping("/find_approved/{post_id}")
    public List<Comment> findAllApprovedCommentsForPostId(@PathVariable("post_id") Integer postId) {
        return commentService.findAllCommentsByForumPostAndStatus(postId, CommentEntity.Status.APPROVED);
    }


    @GetMapping("/user/{user_id}")
    public List<Comment> findAllByUserId(@PathVariable("user_id") Integer userId) {
        return commentService.findAllCommentsByUserId(userId);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment addComment(@Valid @RequestBody CreateCommentRequest request) {
        return commentService.addComment(request);
    }

    @PutMapping("/edit")
    public Comment editComment(@Valid @RequestBody EditCommentRequest request) {
        return commentService.editComment(request);
    }

    @PutMapping("/update")
    public Comment editComment(@Valid @RequestBody UpdateCommentRequest request) {
        return commentService.updateComment(request);
    }

    @PutMapping("/change_status/{comment_id}")
    public Comment changeStatus(@PathVariable("comment_id") Integer commentId,
                                @RequestParam("status") CommentEntity.Status status) {
        return commentService.changeCommentStatus(commentId, status);
    }

    @DeleteMapping("/delete/{id}")
    public Comment deleteComment(@PathVariable("id") Integer id) {
        return commentService.deleteComment(id);
    }
}
