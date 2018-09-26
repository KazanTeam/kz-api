package com.kazan.mapper;

import com.kazan.dto.KazanUserDto;
import com.kazan.model.KazanUser;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class KazanUserMapping {

    public static KazanUserDto entityToDto(KazanUser kazanUser) {
        if (kazanUser != null) {
            KazanUserDto kazanUserDto = new KazanUserDto();
            kazanUserDto.setUserId(kazanUser.getUserId());
            kazanUserDto.setUserName(kazanUser.getUsername());
            kazanUserDto.setFirstName(kazanUser.getFirstname());
            kazanUserDto.setLastName(kazanUser.getLastname());
            kazanUserDto.setEmail(kazanUser.getEmail());
            kazanUserDto.setPhone(kazanUser.getPhone());
            kazanUserDto.setLanguage(kazanUser.getLanguage());
            kazanUserDto.setRefId(kazanUser.getRefId());
            kazanUserDto.setTelegramId(kazanUser.getTelegramId());

            return kazanUserDto;
        }

        return null;
    }

    public static List<KazanUserDto> entitesToDtos(List<KazanUser> kazanUsers) {
        List<KazanUserDto> kazanUserDtos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(kazanUsers)) {
            for (KazanUser kazanUser: kazanUsers) {
                kazanUserDtos.add(entityToDto(kazanUser));
            }
        }

        return kazanUserDtos;
    }

    public static KazanUser dtoToEntity(KazanUserDto kazanUserDto) {
        if (kazanUserDto != null) {
            KazanUser kazanUser = new KazanUser();
            kazanUser.setUserId(kazanUserDto.getUserId());
            kazanUser.setUsername(kazanUserDto.getUserName());
            kazanUser.setFirstname(kazanUserDto.getFirstName());
            kazanUser.setLastname(kazanUserDto.getLastName());
            kazanUser.setEmail(kazanUserDto.getEmail());
            kazanUser.setPhone(kazanUserDto.getPhone());
            kazanUser.setLanguage(kazanUserDto.getLanguage());
            kazanUser.setRefId(kazanUserDto.getRefId());
            kazanUser.setTelegramId(kazanUserDto.getTelegramId());
            if (StringUtils.isNotEmpty(kazanUserDto.getPassword())) {
                kazanUser.setPassword(kazanUserDto.getPassword());
            }

            return kazanUser;
        }

        return null;
    }

}
