package org.etf.unibl.SecureForum.service.impl;

import jakarta.transaction.Transactional;
import org.etf.unibl.SecureForum.base.CrudJpaService;
import org.etf.unibl.SecureForum.exceptions.NotFoundException;
import org.etf.unibl.SecureForum.model.dto.ForumPost;
import org.etf.unibl.SecureForum.model.entities.ForumPostEntity;
import org.etf.unibl.SecureForum.model.entities.PermissionsEntity;
import org.etf.unibl.SecureForum.model.entities.UserEntity;
import org.etf.unibl.SecureForum.model.enums.UserType;
import org.etf.unibl.SecureForum.model.requests.CreatePostRequest;
import org.etf.unibl.SecureForum.model.requests.UpdatePostRequest;
import org.etf.unibl.SecureForum.repositories.ForumPostRepository;
import org.etf.unibl.SecureForum.service.ForumPostService;
import org.etf.unibl.SecureForum.service.PermissionsService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ForumPostImpl extends CrudJpaService<ForumPostEntity, Integer> implements ForumPostService {
    private final ForumPostRepository forumPostRepository;
    private final ModelMapper modelMapper;

    public ForumPostImpl(ForumPostRepository forumPostRepository, ModelMapper modelMapper){
        super(forumPostRepository, modelMapper, ForumPostEntity.class);
        this.modelMapper = modelMapper;
        this.forumPostRepository = forumPostRepository;
    }


    @Override
    public ForumPost addForumPost(CreatePostRequest request) {
        ForumPostEntity entityToAdd = new ForumPostEntity();
        entityToAdd.setTitle(request.getTitle());
        entityToAdd.setContent(request.getContent());
        entityToAdd.setPostCreator(request.getUser());
        entityToAdd.setPostTopic(request.getTopic());
        entityToAdd.setStatus(ForumPostEntity.Status.PENDING);

        entityToAdd.setPostedAt(Timestamp.from(Instant.now()));
        entityToAdd.setModifiedAt(Timestamp.from(Instant.now()));

        ForumPostEntity addedEntity = forumPostRepository.save(entityToAdd);

        //This if checks if the user is an Administrator or a moderator, if he is, his Forum Post ( and comments ) get automatically approved
        if(addedEntity.getPostCreator().getType().equals(UserType.Administrator) || addedEntity.getPostCreator().getType().equals(UserType.Moderator))
        {
            addedEntity.setStatus(ForumPostEntity.Status.APPROVED);
            addedEntity = forumPostRepository.save(addedEntity);
        }

        return mapForumPostEntityToForumPost(addedEntity);

    }

    @Override
    public ForumPost editForumPost(UpdatePostRequest request) {

        ForumPostEntity entityToUpdate = forumPostRepository.findById(request.getId()).orElseThrow(NotFoundException::new);
        entityToUpdate.setTitle(request.getTitle());
        entityToUpdate.setContent(request.getContent());
        entityToUpdate.setPostCreator(request.getUser());
        entityToUpdate.setPostTopic(request.getTopic());

        entityToUpdate.setModifiedAt(Timestamp.from(Instant.now()));

        ForumPostEntity updatedEntity = forumPostRepository.save(entityToUpdate);

        return mapForumPostEntityToForumPost(updatedEntity);
    }

    @Override
    public ForumPost deleteForumPostById(Integer id) {
        ForumPostEntity foundEntity = forumPostRepository.findById(id).orElseThrow(NotFoundException::new);

        forumPostRepository.deleteById(foundEntity.getId());
        return mapForumPostEntityToForumPost(foundEntity);
    }

    @Override
    public ForumPost changeStatus(Integer post_id, ForumPostEntity.Status status){
        ForumPostEntity entityToUpdate = forumPostRepository.findById(post_id).orElseThrow(NotFoundException::new);
        entityToUpdate.setStatus(status);

        ForumPostEntity updatedEntity = forumPostRepository.save(entityToUpdate);
        return mapForumPostEntityToForumPost(updatedEntity);
    }

    /**
     * For mapping into a proper DTO object to return to front users.
     * @param entity the entity from the database to map
     * @return Mapped ForumPost object
     */
    private ForumPost mapForumPostEntityToForumPost(ForumPostEntity entity){
        ForumPost forumPost = new ForumPost();

        forumPost.setId(entity.getId());
        forumPost.setTopic(entity.getPostTopic().getName());
        forumPost.setTitle(entity.getTitle());
        forumPost.setContent(entity.getContent());
        forumPost.setCreated_at(entity.getPostedAt());
        forumPost.setModified_at(entity.getModifiedAt());
        forumPost.setUser_creator(entity.getPostCreator().getUsername());
        forumPost.setStatus(entity.getStatus());

        return forumPost;
    }

    @Override
    public List<ForumPost> findAllByUserId(Integer user_id) {
        List<ForumPostEntity> foundEntities = forumPostRepository.findAllByPostCreator_Id(user_id);
        List<ForumPost> mappedEntities = new ArrayList<>();

        for(ForumPostEntity foundEntity : foundEntities){
            mappedEntities.add(mapForumPostEntityToForumPost(foundEntity));
        }

        return mappedEntities;
    }

    @Override
    public List<ForumPost> findAllByTopicId(Integer topic_id) {
        List<ForumPostEntity> foundEntities = forumPostRepository.findAllByPostTopic_Id(topic_id);
        List<ForumPost> mappedEntities = new ArrayList<>();

        for(ForumPostEntity foundEntity : foundEntities){
            mappedEntities.add(mapForumPostEntityToForumPost(foundEntity));
        }

        return mappedEntities;
    }

    @Override
    public List<ForumPost> findAllForumPosts() {
        List<ForumPostEntity> foundEntities = forumPostRepository.findAll();
        List<ForumPost> mappedEntities = new ArrayList<>();

        for(ForumPostEntity foundEntity : foundEntities){
            mappedEntities.add(mapForumPostEntityToForumPost(foundEntity));
        }

        return mappedEntities;
    }
}
