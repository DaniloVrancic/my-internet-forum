package org.etf.unibl.SecureForum.service.impl;

import jakarta.transaction.Transactional;
import org.etf.unibl.SecureForum.base.CrudJpaService;
import org.etf.unibl.SecureForum.model.entities.TopicEntity;
import org.etf.unibl.SecureForum.repositories.TopicRepository;
import org.etf.unibl.SecureForum.repositories.UserRepository;
import org.etf.unibl.SecureForum.service.TopicService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TopicServiceImpl extends CrudJpaService<TopicEntity, Integer> implements TopicService {

    private final ModelMapper modelMapper;
    private final TopicRepository topicRepository;

    @Autowired
    public TopicServiceImpl(TopicRepository topicRepository, ModelMapper modelMapper){
        super(topicRepository, modelMapper, TopicEntity.class);
        this.topicRepository = topicRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<TopicEntity> findAllByNameContains(String name_part) {
        return topicRepository.findAllByNameContaining(name_part);
    }

    @Override
    public List<TopicEntity> findAllByNameIs(String name) {
        return topicRepository.findAllByNameEquals(name);
    }

    @Override
    public List<TopicEntity> findAll() {
        return topicRepository.findAll();
    }
}
