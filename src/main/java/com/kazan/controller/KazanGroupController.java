package com.kazan.controller;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kazan.component.TelegramSendGroups;
import com.kazan.model.KazanGroup;
import com.kazan.model.UserGroupRole;
import com.kazan.repository.GroupRepository;
import com.kazan.repository.KazanObjectRepository;
import com.kazan.repository.UserGroupRoleRepository;
import com.kazan.repository.UserRepository;
import com.kazan.wrapper.AuthorizationHeaderWrapper;
import com.kazan.wrapper.NewGroupRequestWrapper;
import com.kazan.wrapper.UserAndGroupWrapper;

@RestController
@RequestMapping(path = "/kazan")
public class KazanGroupController {
	@Autowired
	private KazanObjectRepository kazanObjectRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserGroupRoleRepository ugrRepository;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private TelegramSendGroups telegramSenderGroups;

	// API 4
	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST, path = "/group/create")
	public @ResponseBody ResponseEntity<String> createGroup(@RequestHeader("Authorization") String authorizationHeader,
			@RequestBody KazanGroup kazanGroup) {
		try {
			kazanGroup.setGroupId(null);
			kazanGroup.setGroupPrivate(1);
			int groupId = ugrRepository.add(kazanGroup);
			if (-1 == groupId) {
				return new ResponseEntity<String>("Cannot create group!", HttpStatus.UNAUTHORIZED);
			} else {
				return new ResponseEntity<String>("Group created successfully!", HttpStatus.ACCEPTED);
			}
		} catch (Exception e) {
			System.out.println("KazanController.createNewGroupForUser:" + e);
			return new ResponseEntity<String>("Error in creating new group process!", HttpStatus.UNAUTHORIZED);
		}
	}

	// API 7 - last part
	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST, path = "/group/edit")
	public @ResponseBody ResponseEntity<String> editGroup(@RequestHeader("Authorization") String authorizationHeader,
			@RequestBody KazanGroup kazanGroup) {
		KazanGroup checkGroup = groupRepository.getGroupById(kazanGroup.getGroupId());
		if (null == checkGroup) {
			return new ResponseEntity<String>("There is no such group to update!", HttpStatus.UNAUTHORIZED);
		} else {
			kazanGroup.setGroupName(checkGroup.getGroupName());
			groupRepository.update(kazanGroup);
			return new ResponseEntity<String>("Group updated successfully!", HttpStatus.ACCEPTED);
		}
	}
	
	
	// Logic for UGR
	
	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST, path = "/api/groups")
	public @ResponseBody ResponseEntity<String> createNewGroupForUser(
			@RequestHeader("Authorization") String authorizationHeader,
			@RequestBody NewGroupRequestWrapper newGroupRequestWrapper) {
		if (null == newGroupRequestWrapper.getRoleId()) {
			return new ResponseEntity<String>("Missing roleId!", HttpStatus.UNAUTHORIZED);
		}

		String[] split_string = authorizationHeader.split("\\.");
		String base64EncodedHeader = split_string[0];
		String base64EncodedBody = split_string[1];
		String base64EncodedSignature = split_string[2];
		Base64 base64Url = new Base64(true);

		String header = new String(base64Url.decode(base64EncodedHeader));
		String body = new String(base64Url.decode(base64EncodedBody));

		ObjectMapper mapper = new ObjectMapper();
		AuthorizationHeaderWrapper authorizationHeaderWrapper = null;
		try {
			authorizationHeaderWrapper = mapper.readValue(body, AuthorizationHeaderWrapper.class);
		} catch (IOException e) {
			System.out.println("KazanController.createNewGroupForUser:" + e);
			return new ResponseEntity<String>("Cannot read header!", HttpStatus.UNAUTHORIZED);
		}

		int userId = userRepository.getIdByUsername(authorizationHeaderWrapper.getUsername());

		if (-1 == userId) {
			return new ResponseEntity<String>("Username not found!", HttpStatus.UNAUTHORIZED);
		} else {
			try {
				KazanGroup kazanGroup = new KazanGroup();
				kazanGroup
						.setGroupName(authorizationHeaderWrapper.getUsername() + " created at " + new Date().getTime());
				int groupId = ugrRepository.add(kazanGroup, userId, newGroupRequestWrapper.getRoleId());
				if (-1 == groupId) {
					return new ResponseEntity<String>("Cannot create group!", HttpStatus.UNAUTHORIZED);
				} else {
					return new ResponseEntity<String>(mapper.writeValueAsString(kazanGroup), HttpStatus.ACCEPTED);
				}
			} catch (JsonProcessingException e) {
				System.out.println("KazanController.createNewGroupForUser:" + e);
				return new ResponseEntity<String>("Cannot parse the created group!", HttpStatus.UNAUTHORIZED);
			} catch (Exception e) {
				System.out.println("KazanController.createNewGroupForUser:" + e);
				return new ResponseEntity<String>("Error in creating new group process!", HttpStatus.UNAUTHORIZED);
			}
		}
	}

	// API 6, 7, 8 - first part
	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST, path = "/group/getrole")
	public @ResponseBody ResponseEntity<String> getRoleId(@RequestHeader("Authorization") String authorizationHeader,
			@RequestBody UserAndGroupWrapper userAndGroupWrapper) {
		if (null == userAndGroupWrapper.getUserId())
			return new ResponseEntity<String>("No userId!", HttpStatus.UNAUTHORIZED);
		if (null == userAndGroupWrapper.getGroupId())
			return new ResponseEntity<String>("No groupId!", HttpStatus.UNAUTHORIZED);
		int roleId = ugrRepository.getRoleIdByUserIdGroupId(userAndGroupWrapper.getUserId(),
				userAndGroupWrapper.getGroupId());
		if (-1 == roleId)
			return new ResponseEntity<String>("Cannot retrieve roleId!", HttpStatus.UNAUTHORIZED);
		else
			return new ResponseEntity<String>("{roleId:" + roleId + "}", HttpStatus.ACCEPTED);
	}

	// API 6 - last part
	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST, path = "/group/adduser")
	public @ResponseBody ResponseEntity<String> addUserToGroup(
			@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserGroupRole userGroupRole) {
		ugrRepository.add(userGroupRole);
		return new ResponseEntity<String>("User added successfully!", HttpStatus.ACCEPTED);
	}

	// API 8 - last part
	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST, path = "/group/editrole")
	public @ResponseBody ResponseEntity<String> editRoleOfUser(
			@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserGroupRole userGroupRole) {
		if (null == userGroupRole.getUserId())
			return new ResponseEntity<String>("No userId!", HttpStatus.UNAUTHORIZED);
		if (null == userGroupRole.getGroupId())
			return new ResponseEntity<String>("No groupId!", HttpStatus.UNAUTHORIZED);
		int roleId = ugrRepository.getRoleIdByUserIdGroupId(userGroupRole.getUserId(), userGroupRole.getGroupId());
		if (1 == roleId)
			return new ResponseEntity<String>("Group creator cannot change role!!!", HttpStatus.UNAUTHORIZED);
		else {
			ugrRepository.update(userGroupRole);
			return new ResponseEntity<String>("Group role changed successfully!", HttpStatus.ACCEPTED);
		}
	}

	// API 3
	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST, path = "/group/userleave")
	public @ResponseBody ResponseEntity<String> leaveGroup(@RequestHeader("Authorization") String authorizationHeader,
			@RequestBody UserAndGroupWrapper userAndGroupWrapper) {
		int creator = groupRepository.getCreatorByGroupId(userAndGroupWrapper.getGroupId());
		if (-1 == creator) {
			return new ResponseEntity<String>("This group has no creator!!!", HttpStatus.UNAUTHORIZED);
		} else if (userAndGroupWrapper.getUserId().equals(creator)) {
			return new ResponseEntity<String>("Creator cannot leave group!!!", HttpStatus.UNAUTHORIZED);
		} else {
			int deletedRecord = ugrRepository.removeUserFromGroup(userAndGroupWrapper.getUserId(),
					userAndGroupWrapper.getGroupId());
			if (0 < deletedRecord)
				return new ResponseEntity<String>("User left group successfully!", HttpStatus.UNAUTHORIZED);
			else
				return new ResponseEntity<String>("No user left group!!!", HttpStatus.UNAUTHORIZED);
		}
	}
}
