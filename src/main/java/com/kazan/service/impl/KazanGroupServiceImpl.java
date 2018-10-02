package com.kazan.service.impl;

import com.kazan.config.Constants;
import com.kazan.dto.KazanGroupDto;
import com.kazan.mapper.KazanGroupMapping;
import com.kazan.model.KazanGroup;
import com.kazan.repository.jpa.KazanGroupRepository;
import com.kazan.service.KazanGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class KazanGroupServiceImpl implements KazanGroupService {

    @Autowired
    KazanGroupRepository kazanGroupRepository;

    @Override
    public Page<KazanGroup> getAll(PageRequest pageRequest) {
        return kazanGroupRepository.findAll(pageRequest);
    }

    @Override
    public Page<KazanGroup> getAllByUserId(Integer userId, PageRequest pageRequest) {
        return kazanGroupRepository.findByCreator(userId, pageRequest);
    }

    @Override
    public KazanGroupDto create(KazanGroup kazanGroup) {
        kazanGroup.setGroupPrivate(Constants.PRIVATE_GROUP);
        return KazanGroupMapping.entityToDto(kazanGroupRepository.save(kazanGroup));
    }

    @Override
    public KazanGroupDto edit(KazanGroup kazanGroup) {
        return KazanGroupMapping.entityToDto(kazanGroupRepository.save(kazanGroup));
    }

    @Override
    public KazanGroup getGroupById(Integer groupId) {
        return kazanGroupRepository.findOne(groupId);
    }

}
