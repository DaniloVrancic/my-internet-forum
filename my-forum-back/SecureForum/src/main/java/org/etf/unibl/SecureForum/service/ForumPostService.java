package org.etf.unibl.SecureForum.service;

import org.etf.unibl.SecureForum.base.CrudService;
import org.etf.unibl.SecureForum.model.dto.ForumPost;
import org.etf.unibl.SecureForum.model.entities.ForumPostEntity;
import org.etf.unibl.SecureForum.model.requests.CreatePostRequest;
import org.etf.unibl.SecureForum.model.requests.EditPostRequest;
import org.etf.unibl.SecureForum.model.requests.UpdatePostRequest;

import java.util.List;

public interface ForumPostService extends CrudService<Integer> {
    ForumPost addForumPost(CreatePostRequest request);
    ForumPost editForumPost(EditPostRequest request);
    ForumPost deleteForumPostById(Integer id);

    ForumPost updateForumPost(UpdatePostRequest request);

    ForumPost changeStatus(Integer post_id, ForumPostEntity.Status status);

    List<ForumPost> findAllByUserId(Integer user_id);
    List<ForumPost> findAllByTopicId(Integer topic_id);

    List<ForumPost> findAllApprovedByTopicId(Integer topic_id);

    List<ForumPost> findAllForumPosts();

    List<ForumPost> findAllByStatus(ForumPostEntity.Status status);
}
