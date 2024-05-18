package org.etf.unibl.SecureForum.service.impl;

import jakarta.transaction.Transactional;
import org.etf.unibl.SecureForum.base.CrudJpaService;
import org.etf.unibl.SecureForum.exceptions.NotFoundException;
import org.etf.unibl.SecureForum.model.dto.Comment;
import org.etf.unibl.SecureForum.model.entities.CommentEntity;
import org.etf.unibl.SecureForum.model.entities.ForumPostEntity;
import org.etf.unibl.SecureForum.model.enums.UserType;
import org.etf.unibl.SecureForum.model.requests.CreateCommentRequest;
import org.etf.unibl.SecureForum.model.requests.EditCommentRequest;
import org.etf.unibl.SecureForum.model.requests.UpdateCommentRequest;
import org.etf.unibl.SecureForum.repositories.CommentRepository;
import org.etf.unibl.SecureForum.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CommentServiceImpl extends CrudJpaService<CommentEntity, Integer> implements CommentService {

    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, ModelMapper modelMapper){
        super(commentRepository,modelMapper,CommentEntity.class);
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
    }

    public List<Comment> findAllComments(){
        List<CommentEntity> allEntities = commentRepository.findAll();
        List<Comment> listToReturn = new ArrayList<>();

        for(CommentEntity commentEntity : allEntities){
            listToReturn.add(mapCommentEntityToComment(commentEntity));
        }

        return listToReturn;
    }

    public List<Comment> findAllCommentsByForumPostId(Integer forum_post_id){
        List<CommentEntity> allEntities = commentRepository.findAllByReferencedPost_Id(forum_post_id);
        List<Comment> listToReturn = new ArrayList<>();

        for(CommentEntity commentEntity : allEntities){
            listToReturn.add(mapCommentEntityToComment(commentEntity));
        }

        return listToReturn;
    }

    public List<Comment> findAllCommentsByUserId(Integer user_id){
        List<CommentEntity> allEntities = commentRepository.findAllByReferencedUser_id(user_id);
        List<Comment> listToReturn = new ArrayList<>();

        for(CommentEntity commentEntity : allEntities){
            listToReturn.add(mapCommentEntityToComment(commentEntity));
        }

        return listToReturn;
    }

    public Comment addComment(CreateCommentRequest request){
        CommentEntity entityToAdd = new CommentEntity();
        entityToAdd.setContent(request.getContent());
        entityToAdd.setReferencedPost(request.getForum_post());
        entityToAdd.setReferencedUser(request.getUser());

        entityToAdd.setStatus(CommentEntity.Status.PENDING);
        entityToAdd.setPostedAt(Timestamp.from(Instant.now()));
        entityToAdd.setModifiedAt(Timestamp.from(Instant.now()));

        CommentEntity addedEntity = commentRepository.save(entityToAdd);

        //This if checks if the user is an Administrator or a moderator, if he is, his Comment ( or Forum post ) get automatically approved
        if(addedEntity.getReferencedUser().getType().equals(UserType.Administrator) || addedEntity.getReferencedUser().getType().equals(UserType.Moderator))
        {
            addedEntity.setStatus(CommentEntity.Status.APPROVED);
            addedEntity = commentRepository.save(addedEntity);
        }

        return mapCommentEntityToComment(addedEntity);
    }

    public Comment editComment(EditCommentRequest request){
        CommentEntity entityToUpdate = commentRepository.findById(request.getId()).orElseThrow(NotFoundException::new);

        entityToUpdate.setContent(request.getContent());
        entityToUpdate.setModifiedAt(Timestamp.from(Instant.now()));

        CommentEntity editedComment = commentRepository.save(entityToUpdate);

        return mapCommentEntityToComment(editedComment);
    }

    public Comment changeCommentStatus(Integer comment_id, CommentEntity.Status status){
        CommentEntity entityToUpdate = commentRepository.findById(comment_id).orElseThrow(NotFoundException::new);

        entityToUpdate.setStatus(status);
        CommentEntity editedComment = commentRepository.save(entityToUpdate);

        return mapCommentEntityToComment(editedComment);
    }

    public Comment updateComment(UpdateCommentRequest request){
        CommentEntity entityToUpdate = commentRepository.findById(request.getId()).orElseThrow(NotFoundException::new);

        entityToUpdate.setContent(request.getContent());
        entityToUpdate.setStatus(request.getStatus());
        entityToUpdate.setModifiedAt(Timestamp.from(Instant.now())); //Optional, if I don't want to leave a trace of editing with this method I can simply switch it off.

        CommentEntity editedComment = commentRepository.save(entityToUpdate);

        return mapCommentEntityToComment(editedComment);
    }

    public Comment deleteComment(Integer id){
        CommentEntity entityToDelete = commentRepository.findById(id).orElseThrow(NotFoundException::new);
        commentRepository.deleteById(id);
        return mapCommentEntityToComment(entityToDelete);
    }

    private Comment mapCommentEntityToComment(CommentEntity entity){
        Comment mappedComment = new Comment();
        mappedComment.setId(entity.getId());
        mappedComment.setContent(entity.getContent());
        mappedComment.setPostedAt(entity.getPostedAt());
        mappedComment.setModifiedAt(entity.getModifiedAt());
        mappedComment.setUser_id(entity.getReferencedUser().getId());
        mappedComment.setUsername(entity.getReferencedUser().getUsername());
        mappedComment.setStatus(entity.getStatus());

        return mappedComment;
    }


}