package org.etf.unibl.SecureForum.service.impl;

import jakarta.transaction.Transactional;
import org.etf.unibl.SecureForum.base.CrudJpaService;
import org.etf.unibl.SecureForum.model.entities.CommentEntity;
import org.etf.unibl.SecureForum.model.entities.ForumPostEntity;
import org.etf.unibl.SecureForum.repositories.CommentRepository;
import org.etf.unibl.SecureForum.service.CommentService;
import org.etf.unibl.SecureForum.service.ForumPostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
