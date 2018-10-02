package com.kazan.service;

import com.kazan.dto.KazanGroupDto;
import com.kazan.model.KazanGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface KazanGroupService {

    Page<KazanGroup> getAll(PageRequest pageRequest);

    Page<KazanGroup> getAllByUserId(Integer userId, PageRequest pageRequest);

    KazanGroupDto create(KazanGroup kazanGroup);

    KazanGroupDto edit(KazanGroup kazanGroup);

    KazanGroup getGroupById(Integer groupId);
}
