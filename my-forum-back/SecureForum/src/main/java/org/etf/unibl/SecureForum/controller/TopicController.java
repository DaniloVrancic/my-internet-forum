package org.etf.unibl.SecureForum.controller;

import org.etf.unibl.SecureForum.model.entities.TopicEntity;
import org.etf.unibl.SecureForum.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/topics")
public class TopicController {

    private final TopicService topicService;

    @Autowired
    public TopicController(TopicService topicService){
        this.topicService = topicService;
    }

    @GetMapping
    public List<TopicEntity> findAllTopics()
    {
       return this.topicService.findAll();
    }

    @GetMapping("/{id}")
    public TopicEntity findAllTopics(@PathVariable Integer id)
    {
        return this.topicService.findById(id, TopicEntity.class);
    }

    @GetMapping("/contains/{sequence}")
    public List<TopicEntity> findAllByContains(@PathVariable("sequence") String sequence){
        System.out.println(sequence);
        if(sequence.isEmpty()){
            return new ArrayList<>();
        }
        else{
            return this.topicService.findAllByNameContains(sequence);
        }
    }

    @GetMapping("/is/{sequence}")
    public  List<TopicEntity> findAllByNameIs(@PathVariable("sequence") String sequence){
        if(sequence.isEmpty()){
            return new ArrayList<>();
        }
        else{
            return this.topicService.findAllByNameIs(sequence);
        }
    }
}
