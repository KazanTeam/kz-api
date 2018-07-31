package com.kazan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kazan.component.TelegramSendGroups;
import com.kazan.model.KazanUser;
import com.kazan.repository.GroupRepository;
import com.kazan.repository.KazanObjectRepository;
import com.kazan.repository.UserGroupRoleRepository;
import com.kazan.repository.UserRepository;

@RestController
@RequestMapping(path = "/kazan")
public class KazanUserController {

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

	// API 1
	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST, path = "/user/add")
	public @ResponseBody ResponseEntity<String> createUser(@RequestHeader("Authorization") String authorizationHeader,
			@RequestBody KazanUser kazanUser) {
		kazanUser.setUserId(null);
		userRepository.add(kazanUser);
		return new ResponseEntity<String>("User added successfully!", HttpStatus.ACCEPTED);
	}

	// API 2
	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST, path = "/user/edit")
	public @ResponseBody ResponseEntity<String> editUser(@RequestHeader("Authorization") String authorizationHeader,
			@RequestBody KazanUser kazanUser) {
		if (null == kazanUser.getUserId()) {
			return new ResponseEntity<String>("No userId!!!", HttpStatus.UNAUTHORIZED);
		} else {
			if (null == userRepository.getById(kazanUser.getUserId())) {
				return new ResponseEntity<String>("No such user!!!", HttpStatus.UNAUTHORIZED);
			} else {
				userRepository.update(kazanUser);
				return new ResponseEntity<String>("User edited successfully!", HttpStatus.ACCEPTED);
			}
		}
	}

	// New API, 3rd of user API
	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST, path = "/user/get/{userId}")
	public @ResponseBody ResponseEntity<String> getUser(@PathVariable("userId") int userId) {
		if (userId < 1) {
			return new ResponseEntity<String>("Invalid userId!!!", HttpStatus.UNAUTHORIZED);
		} else {
			KazanUser kazanUser = userRepository.getById(userId);
			if (null == kazanUser) {
				return new ResponseEntity<String>("No such user!!!", HttpStatus.UNAUTHORIZED);
			} else {
				try {
					return new ResponseEntity<String>(new ObjectMapper().writeValueAsString(kazanUser), HttpStatus.ACCEPTED);
				} catch (JsonProcessingException e) {
					System.out.println("KazanController.getUser:" + e);
					return new ResponseEntity<String>("Error parsing object!", HttpStatus.UNAUTHORIZED);
				}
			}
		}
	}

}
