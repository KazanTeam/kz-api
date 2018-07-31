package com.kazan.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.kazan.model.KazanObject;
import com.kazan.repository.GroupRepository;
import com.kazan.repository.KazanObjectRepository;
import com.kazan.repository.UserGroupRoleRepository;
import com.kazan.repository.UserRepository;
import com.kazan.wrapper.AlertRequestWrapper;
import com.kazan.wrapper.AuthorizationHeaderWrapper;
import com.kazan.wrapper.ObjectRequestWrapper;

@RestController    
@RequestMapping(path="/kazan")
public class KazanController {
	
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
	
	@RequestMapping(method=RequestMethod.POST, path="/message/add")
	public @ResponseBody ResponseEntity<String> addAlert(@RequestBody AlertRequestWrapper alertWrapper) {
		int userId = userRepository.getIdByUsername(alertWrapper.getUsername());
		if (-1 == userId) {
			return new ResponseEntity<String>("Username not found!", HttpStatus.UNAUTHORIZED);
		}
		
		List<String> groupAliases = alertWrapper.getGroupAliases();
		List<Integer> groupIds = new ArrayList<Integer>();
		int groupId;
		for(String groupAliase:groupAliases) {
			groupId = checkMessagePermission(groupAliase, userId) ;
			if(-1!=groupId) groupIds.add(groupId);
		}
		int TelegramBotType=2;
		telegramSenderGroups.sendMessage(groupIds, alertWrapper.getMode(), TelegramBotType, alertWrapper.getContent(), alertWrapper.getNote(), alertWrapper.getImage());
		return new ResponseEntity<String>("Alert added successfully!", HttpStatus.ACCEPTED);
	}
	
	private int checkMessagePermission(String groupAliase,int userId) {
		int groupId = ugrRepository.getGroupIdByGroupAlias(userId, groupAliase);
		if (-1 == groupId) return -1;
		int roleId = ugrRepository.getByUserIdGroupId(userId, groupId); 
		if(checkSendMessagePermissionByRoleIdAndMode(roleId, 0)) return groupId;
		return -1;
	}
	
	@CrossOrigin
	@RequestMapping(method=RequestMethod.POST, path="/object/syn")
	public @ResponseBody ResponseEntity<String> synObject(@RequestBody ObjectRequestWrapper wrapperObject) {
		int userId = userRepository.getIdByUsername(wrapperObject.getUsername());
		if (-1 == userId) {
			return new ResponseEntity<String>("Username not found!", HttpStatus.UNAUTHORIZED);
		}
		
		List<String> groupAliases = wrapperObject.getGroupAliases();
		List<Integer> groupIds = new ArrayList<Integer>();
		int groupId;
		//Linh Dao update here
		for(String groupAliase:groupAliases) {
			groupId = synObject(groupAliase, userId, wrapperObject.getSymbol(), wrapperObject.getMode(), wrapperObject.getObjects());
			if(-1!=groupId) groupIds.add(groupId);
		}
		if (groupIds.isEmpty())
			return new ResponseEntity<String>("No available group!", HttpStatus.UNAUTHORIZED);
		if(!wrapperObject.getObjects().isEmpty()  && !groupIds.isEmpty()) {
			int TelegramBotType;
			if(wrapperObject.getMode()==3) {
				TelegramBotType =1;
			} else {
				TelegramBotType =2;
			}
			String content = wrapperObject.getUsername() + " - " + wrapperObject.getSymbol() + " _" + wrapperObject.getPeriod();
			telegramSenderGroups.sendMessage(groupIds, wrapperObject.getMode(), TelegramBotType, content, wrapperObject.getNote(), wrapperObject.getImage());
		}
		
		return new ResponseEntity<String>("Object list synchronized!", HttpStatus.ACCEPTED);
	}
	
	private int synObject(String groupAliase,int userId, String symbol, int mode, List<KazanObject> objects) {
		int groupId = ugrRepository.getGroupIdByGroupAlias(userId, groupAliase);
		if (-1 == groupId) return -1;
		int roleId = ugrRepository.getByGroupIdUserIdSymbol(userId, groupId, symbol); 
		if(checkPushPermissionByRoleIdAndMode(roleId, mode)) {
			try {
				//Linh Dao remove it
				//if (2 <= mode && mode <= 5) {
					kazanObjectRepository.deleteBySymbolUserGroupMode(symbol, userId, groupId, mode);
				//}
			} catch(Exception e) {
				System.out.println("KazanController.synObject:" + e);
				return -1;
			}
			if (objects != null && !objects.isEmpty()) {
				Date objectUdatedDate = new Date();
				for (KazanObject ko : objects) {
					ko.setSymbol(symbol);
					ko.setUserId(userId);
					ko.setGroupId(groupId);
					ko.setUpdated_date(objectUdatedDate);
					ko.setModeId(mode);
					kazanObjectRepository.add(ko);
				}
			}
			return groupId;
		}
		return -1;
	}
	
	
	
	@CrossOrigin
	@RequestMapping(method=RequestMethod.POST, path="/object/get")
	public @ResponseBody ResponseEntity<String> getObject(@RequestBody ObjectRequestWrapper wrapperObject) {
		int userId = userRepository.getIdByUsername(wrapperObject.getUsername());
		List<String> groupAliases = wrapperObject.getGroupAliases();
		
		if (-1 == userId) {
			return new ResponseEntity<String>("Username not found!", HttpStatus.UNAUTHORIZED);
		}
		String groupAliase = groupAliases.get(0);
		int groupId = ugrRepository.getGroupIdByGroupAlias(userId, groupAliase);
		if (-1 == groupId) {
			return new ResponseEntity<String>("Group not found!", HttpStatus.UNAUTHORIZED);
		}
		int roleId = ugrRepository.getByGroupIdUserIdSymbol(userId, groupId, wrapperObject.getSymbol()); 
		
		if(checkGetPermissionByRoleIdAndMode(roleId, wrapperObject.getMode())) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				//Linh Dao update here
				int getFromUserId=-1;
				 
				if(! "".equalsIgnoreCase(wrapperObject.getGetFromUser())) {
					getFromUserId = userRepository.getIdByUsername(wrapperObject.getGetFromUser());
				}
				int mode_id = wrapperObject.getMode();
				if(-1 == getFromUserId) {
					String[][] userUpdate = kazanObjectRepository.getUserIdAndUpdateTime(wrapperObject.getSymbol(), groupId, roleId);
					if (userUpdate.length > 0 && userUpdate[0].length > 0){
 						getFromUserId = userRepository.getIdByUsername(userUpdate[0][0]);
						mode_id = Integer.parseInt(userUpdate[0][1]); // lay userId và mode cua user cuoi cung
					}
				}
				
				if( mode_id>=2 && mode_id <= 4) {
					return new ResponseEntity<String>(mapper.writeValueAsString(kazanObjectRepository.getBySymbolUserGroup(wrapperObject.getSymbol(), getFromUserId, groupId, mode_id)), HttpStatus.ACCEPTED);
				} else {
					return new ResponseEntity<String>("Invalid mode!", HttpStatus.UNAUTHORIZED);
				}
				//End update
			} catch (JsonProcessingException e) {
				System.out.println("KazanController.getObject:" + e);
				return new ResponseEntity<String>("Error parsing object!", HttpStatus.UNAUTHORIZED);
			}
		} else {
			return new ResponseEntity<String>("Error getting object!", HttpStatus.UNAUTHORIZED);
		}
		
	}
	/**
	Lay list nhung user da day len trong qua khu
	Du lieu duoc lay theo cau lenh sau
	select mode_id, user_id, updated_date from object where group_id=1 and symbol = 'USDJPY' and mode_id >=role
	group by updated_date, user_id, mode_id
	order by updated_date
	**/
	@CrossOrigin
	@RequestMapping(method=RequestMethod.POST, path="/user/get")
	public @ResponseBody ResponseEntity<String> userGet(@RequestBody ObjectRequestWrapper wrapperObject) {
		//=============== sau nay phan nay se duoc config trong payment ====================
		int configForMaxReturnUser = 10;
		//===================================
		int userId = userRepository.getIdByUsername(wrapperObject.getUsername());
		List<String> groupAliases = wrapperObject.getGroupAliases();
		if (-1 == userId) {
			return new ResponseEntity<String>("Username not found!", HttpStatus.UNAUTHORIZED);
		}
		String groupAliase = groupAliases.get(0);
		int groupId = ugrRepository.getGroupIdByGroupAlias(userId, groupAliase);
		if (-1 == groupId) {
			return new ResponseEntity<String>("Group not found!", HttpStatus.UNAUTHORIZED);
		}
		int roleId = ugrRepository.getByGroupIdUserIdSymbol(userId, groupId, wrapperObject.getSymbol());
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			String[][] userObjects = null;
			userObjects  = kazanObjectRepository.getUserIdAndUpdateTime(wrapperObject.getSymbol(), groupId, roleId);
			return new ResponseEntity<String>(mapper.writeValueAsString(userObjects), HttpStatus.ACCEPTED);
		} catch (JsonProcessingException e) {
			System.out.println("KazanController.getObject:" + e);
			return new ResponseEntity<String>("Error getting object!", HttpStatus.UNAUTHORIZED);
		}
		
	}

	boolean checkPushPermissionByRoleIdAndMode(int roleId, int mode) {
		if(roleId==4 || roleId==5) return false;
		if(roleId==3 && mode==3) return true;
		if(roleId==2) return true;
		return false;
	} 
	boolean checkGetPermissionByRoleIdAndMode(int roleId, int mode) {
		if (mode > 1 && roleId <= mode) return true;
		return false;
	}
	boolean checkSendMessagePermissionByRoleIdAndMode(int roleId, int mode) {
		if(roleId==2) return true;
		return false;
	}
	
	@CrossOrigin
	@RequestMapping(method=RequestMethod.GET, path="/api/users/groups")
	public @ResponseBody ResponseEntity<String> getAllGroupsByUser(@RequestHeader("Authorization") String authorizationHeader) {		
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
			System.out.println("KazanController.getAllGroupsByUser:" + e);
			return new ResponseEntity<String>("Cannot read header!", HttpStatus.UNAUTHORIZED);
		}
		
		int userId = userRepository.getIdByUsername(authorizationHeaderWrapper.getUsername());

		if (-1 == userId) {
			return new ResponseEntity<String>("Username not found!", HttpStatus.UNAUTHORIZED);
		}
		else {
			try {
				return new ResponseEntity<String>(mapper.writeValueAsString(groupRepository.getGroupByIdList(ugrRepository.getGroupIdListByUserId(userId))), HttpStatus.ACCEPTED);
			} catch (JsonProcessingException e) {
				System.out.println("KazanController.getAllGroupsByUser:" + e);
				return new ResponseEntity<String>("Cannot get groups!", HttpStatus.UNAUTHORIZED);
			}
		}
	}
	
	
	
}
