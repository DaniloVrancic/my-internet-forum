package org.etf.unibl.SecureForum.service;

import org.etf.unibl.SecureForum.base.CrudService;
import org.etf.unibl.SecureForum.model.dto.Comment;
import org.etf.unibl.SecureForum.model.entities.CommentEntity;
import org.etf.unibl.SecureForum.model.entities.ForumPostEntity;
import org.etf.unibl.SecureForum.model.requests.CreateCommentRequest;
import org.etf.unibl.SecureForum.model.requests.EditCommentRequest;
import org.etf.unibl.SecureForum.model.requests.UpdateCommentRequest;

import java.util.List;

public interface CommentService extends CrudService<Integer> {

    List<Comment> findAllComments();
    List<Comment> findAllCommentsByForumPostId(Integer forum_post_id);
    List<Comment> findAllCommentsByUserId(Integer user_id);

    List<Comment> findAllCommentsByForumPostAndStatus(Integer forum_post_id, CommentEntity.Status status);

    List<Comment> findAllByStatus(CommentEntity.Status status);

    Comment findByCommentId(Integer comment_id);
    Comment addComment(CreateCommentRequest request);
    Comment editComment(EditCommentRequest request);
    Comment changeCommentStatus(Integer comment_id, CommentEntity.Status status);
    Comment updateComment(UpdateCommentRequest request);
    Comment deleteComment(Integer id);
}
