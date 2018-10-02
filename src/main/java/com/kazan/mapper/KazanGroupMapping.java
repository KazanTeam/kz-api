package com.kazan.mapper;

import com.kazan.dto.KazanGroupDto;
import com.kazan.model.KazanGroup;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class KazanGroupMapping {

    public static List<KazanGroupDto> entitesToDtos(List<KazanGroup> groups) {
        List<KazanGroupDto> dtos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(groups)) {
            for(KazanGroup kazanGroup : groups) {
                dtos.add(entityToDto(kazanGroup));
            }
        }

        return dtos;
    }

    public static KazanGroup dtoToEntity(KazanGroupDto kazanGroupDto) {
        if (kazanGroupDto == null) {
            return null;
        }

        KazanGroup kazanGroup = new KazanGroup();


        return kazanGroup;
    }

    public static KazanGroupDto entityToDto(KazanGroup kazanGroup) {
        if (kazanGroup == null) {
            return null;
        }

        KazanGroupDto kazanGroupDto = new KazanGroupDto();

        return kazanGroupDto;
    }

}
