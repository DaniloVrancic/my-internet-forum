package org.etf.unibl.SecureForum.service;

import org.etf.unibl.SecureForum.base.CrudService;
import org.etf.unibl.SecureForum.model.dto.ForumPost;
import org.etf.unibl.SecureForum.model.requests.CreatePostRequest;
import org.etf.unibl.SecureForum.model.requests.UpdatePostRequest;

import java.util.List;

public interface ForumPostService extends CrudService<Integer> {
    ForumPost addForumPost(CreatePostRequest request);
    ForumPost editForumPost(UpdatePostRequest request);
    ForumPost deleteForumPostById(Integer id);

    List<ForumPost> findAllByUserId(Integer user_id);
    List<ForumPost> findAllByTopicId(Integer topic_id);

    List<ForumPost> findAllForumPosts();
}
