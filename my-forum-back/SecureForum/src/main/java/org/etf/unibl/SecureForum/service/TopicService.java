package org.etf.unibl.SecureForum.service;

import org.etf.unibl.SecureForum.base.CrudService;
import org.etf.unibl.SecureForum.model.entities.TopicEntity;

import java.util.List;

public interface TopicService extends CrudService<Integer> {

    List<TopicEntity> findAllByNameContains(String name_part);

    List<TopicEntity> findAllByNameIs(String name);

    List<TopicEntity> findAll();
}
