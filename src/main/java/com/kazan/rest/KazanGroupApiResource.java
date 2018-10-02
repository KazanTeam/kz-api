package com.kazan.rest;

import com.kazan.config.Constants;
import com.kazan.dto.KazanGroupDto;
import com.kazan.dto.ResponseDto;
import com.kazan.mapper.KazanGroupMapping;
import com.kazan.model.KazanGroup;
import com.kazan.service.KazanGroupService;
import com.kazan.service.KazanUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/kazan/api")
public class KazanGroupApiResource {

    @Autowired
    KazanGroupService kazanGroupService;

    @Autowired
    KazanUserService kazanUserService;

    @RequestMapping(method = RequestMethod.GET, path = "/group/{groupId}")
    public ResponseEntity<ResponseDto<KazanGroupDto>> getById(@PathVariable("groupId") int groupId) {
        ResponseDto<KazanGroupDto> response = new ResponseDto<>();
        response.setContent(KazanGroupMapping.entityToDto(kazanGroupService.getGroupById(groupId)));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/group")
    public ResponseEntity<ResponseDto<KazanGroupDto>> create(@RequestBody KazanGroupDto kazanGroupDto) {
        ResponseDto<KazanGroupDto> response = new ResponseDto<>();
        response.setContent(kazanGroupService.create(KazanGroupMapping.dtoToEntity(kazanGroupDto)));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/group")
    public ResponseEntity<ResponseDto<KazanGroupDto>> edit(@RequestBody KazanGroupDto kazanGroupDto) {
        ResponseDto<KazanGroupDto> response = new ResponseDto<>();
        KazanGroup kazanGroupExist = kazanGroupService.getGroupById(kazanGroupDto.getGroupId());
        if (kazanGroupExist == null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        KazanGroup kazanGroup = KazanGroupMapping.dtoToEntity(kazanGroupDto);

        response.setContent(kazanGroupService.edit(kazanGroup));

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/group/all")
    public ResponseEntity<ResponseDto<List<KazanGroupDto>>> getAllGroups(
            @RequestParam(value = "index", defaultValue = Constants.DEFAULT_PAGE_INDEX) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_RECORD_IN_PAGE) int size) {

        ResponseDto<List<KazanGroupDto>> response = new ResponseDto<>();
        PageRequest pageRequest = new PageRequest(page, size);

        Page<KazanGroup> pageKazanGroup = kazanGroupService.getAll(pageRequest);
        response.setContent(KazanGroupMapping.entitesToDtos(pageKazanGroup.getContent()));
        response.setTotalRecord(pageKazanGroup.getTotalElements());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/group/groups-by-member-id/{userId}")
    public ResponseEntity<ResponseDto<List<KazanGroupDto>>> getAllGroupsByMemberId(
            @PathVariable("userId") int userId,
            @RequestParam(value = "index", defaultValue = Constants.DEFAULT_PAGE_INDEX) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_RECORD_IN_PAGE) int size) {

        ResponseDto<List<KazanGroupDto>> response = new ResponseDto<>();

        if (kazanUserService.findById(userId) == null) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        PageRequest pageRequest = new PageRequest(page, size);
        Page<KazanGroup> pageKazanGroup = kazanGroupService.getAllByUserId(userId, pageRequest);
        response.setContent(KazanGroupMapping.entitesToDtos(pageKazanGroup.getContent()));
        response.setTotalRecord(pageKazanGroup.getTotalElements());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/group/users-in-group/{groupId}")
    public ResponseEntity<ResponseDto<List<KazanGroupDto>>> getUsersInGroup(
            @PathVariable("groupId") int groupId,
            @RequestParam(value = "index", defaultValue = Constants.DEFAULT_PAGE_INDEX) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_RECORD_IN_PAGE) int size) {

        ResponseDto<List<KazanGroupDto>> response = new ResponseDto<>();
        PageRequest pageRequest = new PageRequest(page, size);

        if (kazanGroupService.getGroupById(groupId) == null) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
