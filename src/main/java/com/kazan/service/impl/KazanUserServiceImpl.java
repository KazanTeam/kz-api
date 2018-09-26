package com.kazan.service.impl;

import com.kazan.dto.KazanUserDto;
import com.kazan.mapper.KazanUserMapping;
import com.kazan.model.KazanUser;
import com.kazan.repository.jpa.KazanUserRepository;
import com.kazan.service.KazanUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class KazanUserServiceImpl implements KazanUserService {

    @Autowired
    private KazanUserRepository kazanUserRepository;

    @Override
    public KazanUserDto createUser(KazanUserDto kazanUserDto) {
        KazanUser kazanUser = KazanUserMapping.dtoToEntity(kazanUserDto);
        kazanUser.setUserId(null);

        kazanUser = kazanUserRepository.saveAndFlush(kazanUser);

        return KazanUserMapping.entityToDto(kazanUser);
    }

    @Override
    public KazanUserDto update(KazanUserDto kazanUserDto) {
        KazanUser kazanUser = KazanUserMapping.dtoToEntity(kazanUserDto);
        if (StringUtils.isEmpty(kazanUser.getPassword())) {
            kazanUser.setPassword(kazanUserRepository.findPasswordByMemberId(kazanUser.getUserId()));
        }

        kazanUser = kazanUserRepository.saveAndFlush(kazanUser);

        return KazanUserMapping.entityToDto(kazanUser);
    }

    @Override
    public KazanUserDto findById(Integer userId) {
        KazanUser kazanUser = kazanUserRepository.findOne(userId);
        return KazanUserMapping.entityToDto(kazanUser);
    }

}
