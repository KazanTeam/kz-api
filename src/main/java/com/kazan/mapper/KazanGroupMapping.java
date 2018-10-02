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
        kazanGroup.setGroupId(kazanGroupDto.getGroupId());
        kazanGroup.setGroupName(kazanGroupDto.getGroupName());
        kazanGroup.setGroupNotifyBot(kazanGroupDto.getGroupNotifyBot());
        kazanGroup.setGroupAlertBot(kazanGroupDto.getGroupAlertBot());
        kazanGroup.setGroupImage(kazanGroupDto.getGroupImage());
        kazanGroup.setMt4Account(kazanGroupDto.getMt4Account());
        kazanGroup.setMt4Server(kazanGroupDto.getMt4Server());
        kazanGroup.setMt4Password(kazanGroupDto.getMt4Password());
        kazanGroup.setNotifyValue(kazanGroupDto.getNotifyValue());
        kazanGroup.setNotifyObjectType(kazanGroupDto.getNotifyObjectType());
        kazanGroup.setNotifyReTime(kazanGroupDto.getNotifyReTime());
        kazanGroup.setCreator(kazanGroupDto.getCreator());
        kazanGroup.setGroupPrivate(kazanGroupDto.getGroupPrivate());

        return kazanGroup;
    }

    public static KazanGroupDto entityToDto(KazanGroup kazanGroup) {
        if (kazanGroup == null) {
            return null;
        }

        KazanGroupDto kazanGroupDto = new KazanGroupDto();
        kazanGroupDto.setGroupId(kazanGroup.getGroupId());
        kazanGroupDto.setGroupName(kazanGroup.getGroupName());
        kazanGroupDto.setGroupNotifyBot(kazanGroup.getGroupNotifyBot());
        kazanGroupDto.setGroupAlertBot(kazanGroup.getGroupAlertBot());
        kazanGroupDto.setGroupImage(kazanGroup.getGroupImage());
        kazanGroupDto.setMt4Account(kazanGroup.getMt4Account());
        kazanGroupDto.setMt4Server(kazanGroup.getMt4Server());
        kazanGroupDto.setMt4Password(kazanGroup.getMt4Password());
        kazanGroupDto.setNotifyValue(kazanGroup.getNotifyValue());
        kazanGroupDto.setNotifyObjectType(kazanGroup.getNotifyObjectType());
        kazanGroupDto.setNotifyReTime(kazanGroup.getNotifyReTime());
        kazanGroupDto.setCreator(kazanGroup.getCreator());
        kazanGroupDto.setGroupPrivate(kazanGroup.getGroupPrivate());

        return kazanGroupDto;
    }

}
