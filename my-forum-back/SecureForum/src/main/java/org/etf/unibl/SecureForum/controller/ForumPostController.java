package org.etf.unibl.SecureForum.controller;

import jakarta.validation.Valid;
import org.etf.unibl.SecureForum.model.dto.Comment;
import org.etf.unibl.SecureForum.model.dto.ForumPost;
import org.etf.unibl.SecureForum.model.entities.ForumPostEntity;
import org.etf.unibl.SecureForum.model.requests.CreatePostRequest;
import org.etf.unibl.SecureForum.model.requests.EditPostRequest;
import org.etf.unibl.SecureForum.model.requests.UpdateCommentRequest;
import org.etf.unibl.SecureForum.model.requests.UpdatePostRequest;
import org.etf.unibl.SecureForum.service.ForumPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/forum_post")
@Validated
public class ForumPostController {
    private final ForumPostService forumPostService;

    @Autowired
    public ForumPostController(ForumPostService forumPostService){
        this.forumPostService = forumPostService;
    }

    @GetMapping
    public List<ForumPost> findAllForumPosts() {
        return forumPostService.findAllForumPosts();
    }

    @GetMapping("/{id}")
    public ForumPost findByPostId(@PathVariable("id") Integer id) {
        return forumPostService.findByPostId(id);
    }

    @GetMapping("/topic/{topic_id}")
    public List<ForumPost> findAllByTopicId(@PathVariable("topic_id") Integer topic_id) {
        return forumPostService.findAllByTopicId(topic_id);
    }

    @GetMapping("/topic_top20/{topic_id}")
    public List<ForumPost> findAllByTopicIdTop20(@PathVariable("topic_id") Integer topic_id) {
        return forumPostService.findAllByTopicId(topic_id).stream().limit(20).collect(Collectors.toList());
    }

    @GetMapping("/topic_approved/{topic_id}")
    public List<ForumPost> findAllApprovedByTopicId(@PathVariable("topic_id") Integer topic_id){
        return forumPostService.findAllApprovedByTopicId(topic_id);
    }

    @GetMapping("/topic_approved_top20/{topic_id}")
    public List<ForumPost> findAllApprovedByTopicIdTop20(@PathVariable("topic_id") Integer topic_id){
        return forumPostService.findAllApprovedByTopicId(topic_id).stream().limit(20).collect(Collectors.toList());
    }

    @GetMapping("/status_pending")
    public List<ForumPost> findAllByStatus(){
        return forumPostService.findAllByStatus(ForumPostEntity.Status.PENDING);
    }

    @GetMapping("/user/{user_id}")
    public List<ForumPost> findAllByUserId(@PathVariable("user_id") Integer user_id) {
        return forumPostService.findAllByUserId(user_id);
    }


    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ForumPost addForumPost(@Valid @RequestBody CreatePostRequest request) {
        return forumPostService.addForumPost(request);
    }

    @PutMapping("/edit")
    public ForumPost editForumPost(@Valid @RequestBody EditPostRequest request) {
        return forumPostService.editForumPost(request);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ForumPost editComment(@Valid @RequestBody UpdatePostRequest request) {
        return forumPostService.updateForumPost(request);
    }

    @PutMapping("/change_status/{post_id}")
    public ForumPost changeStatus(@PathVariable("post_id") Integer postId,
                                @RequestParam("status") ForumPostEntity.Status status) {
        return forumPostService.changeStatus(postId, status);
    }

    @DeleteMapping("/delete/{id}")
    public ForumPost deleteForumPost(@PathVariable("id") Integer id) {
        return forumPostService.deleteForumPostById(id);
    }
}
