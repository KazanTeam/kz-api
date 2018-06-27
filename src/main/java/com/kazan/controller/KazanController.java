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
import com.kazan.component.TelegramSender;
import com.kazan.model.KazanGroup;
import com.kazan.model.KazanObject;
import com.kazan.model.KazanUser;
import com.kazan.model.Message;
import com.kazan.model.UserGroupRole;
import com.kazan.repository.GroupRepository;
import com.kazan.repository.KazanObjectRepository;
import com.kazan.repository.UserGroupRoleRepository;
import com.kazan.repository.UserRepository;
import com.kazan.wrapper.AlertRequestWrapper;
import com.kazan.wrapper.AuthorizationHeaderWrapper;
import com.kazan.wrapper.NewGroupRequestWrapper;
import com.kazan.wrapper.ObjectRequestWrapper;
import com.kazan.wrapper.UserAndGroupWrapper;

import ch.qos.logback.core.joran.conditional.ElseAction;

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
	private TelegramSender telegramSender;
	
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
		sendMessage(groupIds, alertWrapper.getMode(), TelegramBotType, alertWrapper.getContent(), alertWrapper.getNote(), alertWrapper.getImage());
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
			sendMessage(groupIds, wrapperObject.getMode(), TelegramBotType, content, wrapperObject.getNote(), wrapperObject.getImage());
		}
		
		return new ResponseEntity<String>("Object list synchronized!", HttpStatus.ACCEPTED);
	}
	
	private int synObject(String groupAliase,int userId, String symbol, int mode, List<KazanObject> objects) {
		int groupId = ugrRepository.getGroupIdByGroupAlias(userId, groupAliase);
		if (-1 == groupId) return -1;
		int roleId = ugrRepository.getByGroupIdUserIdSymbol(userId, groupId, symbol); 
		if(checkPushPermissionByRoleIdAndMode(roleId, mode)) {
			try {
				if (2 <= mode && mode <= 5) {
					kazanObjectRepository.deleteBySymbolUserGroupMode(symbol, userId, groupId, mode);
				}
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
	
	private void sendMessage(List<Integer> groupIds, int mode, int TelegramBotType, String content, String  note, String imageUrl) {
		List<Message> alertList = new ArrayList<Message>();
		int telegramId;
		String telegramTokenBot;
		for(int groupId:groupIds) {
			KazanGroup groupObject =  groupRepository.getGroupById(groupId);
			List<Integer> listUserId = ugrRepository.getUserIdByGroupIdAndMode(groupId, mode);
			telegramTokenBot = groupObject.getTokenBot(TelegramBotType);
			for(int userId: listUserId) {				
				telegramId = userRepository.geTelegramId(userId);
				if(!"".equalsIgnoreCase(telegramTokenBot) && -1!=userId) {
					Message newAlert = new Message();
					newAlert.setMessageTime(new Date());
					newAlert.setNote(note);
					newAlert.setContent(content);
					newAlert.setImageUrl(imageUrl);
					newAlert.setTelegramId(telegramId);
					newAlert.setGroupName(groupObject.getGroupName());
					newAlert.setCountSend(0);
					newAlert.setTelegramTokenBot(telegramTokenBot);
					newAlert.setMessageType(mode);
					alertList.add(newAlert);
				}
			}	
		}
		alertList = removeDuplicateMessage(alertList);
		sendMessagetoTelegram(alertList);
		
	}
	private void sendMessagetoTelegram(List<Message> listMessage) {
		String sendedContent = "";
		for(Message message:listMessage) {
			sendedContent = message.getGroupName().toUpperCase();
			if(message.getCountSend()>0) {
				sendedContent += " AND "+ Integer.toString(message.getCountSend()) + " MORE";
			}
			if(message.getMessageType()==2) {
				sendedContent+= " MASTER: ";
			} else if(message.getMessageType()==3) {
				sendedContent+= " : ";
			} else if(message.getMessageType()==4 || message.getMessageType()==5) {
				sendedContent+= " ALERT : ";
			}
			if (null != message.getNote())
				sendedContent+= message.getNote();
			sendedContent+= System.lineSeparator() + message.getContent();
			if(null != message.getImageUrl() && !"".equalsIgnoreCase(message.getImageUrl())) {
		        sendedContent+= System.lineSeparator() + System.lineSeparator() + message.getImageUrl();
		    }
			
			telegramSender.sendMessage(message.getTelegramTokenBot(), message.getTelegramId().toString(), sendedContent);
			System.out.println("send to "+ message.getTelegramId() +"by "+ message.getTelegramTokenBot()+" message :"+sendedContent);
			
		}
	}
	
	//Remove duplicate Message list
	// If Message allready exist  countSend++
	private boolean existOnMessageList(List<Message> listMessage, Message checkMessage ) {
		for(int i=0 ;i<listMessage.size();i++) {
			if(listMessage.get(i).equals(checkMessage)) {
				listMessage.get(i).addCountSend();
				return true;
			} 
		}
		return false;
	}
	private List<Message> removeDuplicateMessage(List<Message> inputMessageList){
		List<Message> reslut = new ArrayList<Message>();
		for(Message message: inputMessageList) {
			if(!existOnMessageList(reslut ,message)) {
				reslut.add(message);
			}
		}
		return reslut;
	}
	//Remove duplicate Message list end
	
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
				if(! "".equalsIgnoreCase(wrapperObject.getGetFromUser()) && wrapperObject.getMode()==3) {
					getFromUserId = userRepository.getIdByUsername(wrapperObject.getGetFromUser());
				}
				int mode_id = wrapperObject.getMode();
				if(-1 == getFromUserId) {
					String[][] userUpdate = kazanObjectRepository.getUserIdAndUpdateTime(wrapperObject.getSymbol(), groupId);
					if (userUpdate.length > 0 && userUpdate[0].length > 0){
 						getFromUserId = userRepository.getIdByUsername(userUpdate[0][0]);
						mode_id = Integer.parseInt(userUpdate[0][1]); // lay userId và mode cua user cuoi cung
					}
				}
				
				if(2 <= wrapperObject.getMode() && wrapperObject.getMode() <= 4) {
					return new ResponseEntity<String>(mapper.writeValueAsString(kazanObjectRepository.getBySymbolUserGroup(wrapperObject.getSymbol(), getFromUserId, groupId)), HttpStatus.ACCEPTED);
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
		int mode = wrapperObject.getMode();
		if(checkUserGetPermissionByRoleId(roleId)) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				String[][] userObjects = null;
				if (2 == mode || 3 == mode)
					userObjects  = kazanObjectRepository.getUserIdAndUpdateTime(wrapperObject.getSymbol(), groupId);
				return new ResponseEntity<String>(mapper.writeValueAsString(userObjects), HttpStatus.ACCEPTED);
			} catch (JsonProcessingException e) {
				System.out.println("KazanController.getObject:" + e);
				return new ResponseEntity<String>("Error getting object!", HttpStatus.UNAUTHORIZED);
			}
		} else {
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
	boolean checkUserGetPermissionByRoleId(int roleId) {
		if(roleId==2 || roleId==3) return true;
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
	
	@CrossOrigin
	@RequestMapping(method=RequestMethod.POST, path="/api/groups")
	public @ResponseBody ResponseEntity<String> createNewGroupForUser(@RequestHeader("Authorization") String authorizationHeader, @RequestBody NewGroupRequestWrapper newGroupRequestWrapper) {
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
		}
		else {
			try {
				KazanGroup kazanGroup = new KazanGroup();
				kazanGroup.setGroupName(authorizationHeaderWrapper.getUsername() + " created at " + new Date().getTime());
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
	
	// API 1
	@CrossOrigin
	@RequestMapping(method=RequestMethod.POST, path="/user/add")
	public @ResponseBody ResponseEntity<String> createUser(@RequestHeader("Authorization") String authorizationHeader, @RequestBody KazanUser kazanUser) {
		kazanUser.setUserId(null);
		userRepository.add(kazanUser);
		return new ResponseEntity<String>("User added successfully!", HttpStatus.ACCEPTED);
	}
	
	// API 2
	@CrossOrigin
	@RequestMapping(method=RequestMethod.POST, path="/user/edit")
	public @ResponseBody ResponseEntity<String> editUser(@RequestHeader("Authorization") String authorizationHeader, @RequestBody KazanUser kazanUser) {
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
	
	// API 3
	@CrossOrigin
	@RequestMapping(method=RequestMethod.POST, path="/group/userleave")
	public @ResponseBody ResponseEntity<String> leaveGroup(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserAndGroupWrapper userAndGroupWrapper) {
		int creator = groupRepository.getCreatorByGroupId(userAndGroupWrapper.getGroupId());
		if (-1 == creator) {
			return new ResponseEntity<String>("This group has no creator!!!", HttpStatus.UNAUTHORIZED);
		} else if (userAndGroupWrapper.getUserId().equals(creator)) {
			return new ResponseEntity<String>("Creator cannot leave group!!!", HttpStatus.UNAUTHORIZED);
		} else {
			int deletedRecord = ugrRepository.removeUserFromGroup(userAndGroupWrapper.getUserId(), userAndGroupWrapper.getGroupId());
			if (0 < deletedRecord)
				return new ResponseEntity<String>("User left group successfully!", HttpStatus.UNAUTHORIZED);
			else 
				return new ResponseEntity<String>("No user left group!!!", HttpStatus.UNAUTHORIZED);
		}
	}	
	
	// API 4
	@CrossOrigin
	@RequestMapping(method=RequestMethod.POST, path="/group/create")
	public @ResponseBody ResponseEntity<String> createGroup(@RequestHeader("Authorization") String authorizationHeader, @RequestBody KazanGroup kazanGroup) {		
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
	
	// API 6, 7, 8 - first part
	@CrossOrigin
	@RequestMapping(method=RequestMethod.POST, path="/role/id")
	public @ResponseBody ResponseEntity<String> getRoleId(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserAndGroupWrapper userAndGroupWrapper) {
		if (null == userAndGroupWrapper.getUserId())
			return new ResponseEntity<String>("No userId!", HttpStatus.UNAUTHORIZED);
		if (null == userAndGroupWrapper.getGroupId())
			return new ResponseEntity<String>("No groupId!", HttpStatus.UNAUTHORIZED);
		int roleId = ugrRepository.getRoleIdByUserIdGroupId(userAndGroupWrapper.getUserId(), userAndGroupWrapper.getGroupId());
		if (-1 == roleId)
			return new ResponseEntity<String>("Cannot retrieve roleId!", HttpStatus.UNAUTHORIZED);
		else
			return new ResponseEntity<String>("{roleId:" + roleId + "}", HttpStatus.ACCEPTED);
	}	
	
	// API 6 - last part
	@CrossOrigin
	@RequestMapping(method=RequestMethod.POST, path="/group/adduser")
	public @ResponseBody ResponseEntity<String> addUserToGroup(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserGroupRole userGroupRole) {
		ugrRepository.add(userGroupRole);
		return new ResponseEntity<String>("User added successfully!", HttpStatus.ACCEPTED);
	}	
	
	// API 7 - last part
	@CrossOrigin
	@RequestMapping(method=RequestMethod.POST, path="/group/edit")
	public @ResponseBody ResponseEntity<String> editGroup(@RequestHeader("Authorization") String authorizationHeader, @RequestBody KazanGroup kazanGroup) {
		KazanGroup checkGroup = groupRepository.getGroupById(kazanGroup.getGroupId());
		if (null == checkGroup) {
			return new ResponseEntity<String>("There is no such group to update!", HttpStatus.UNAUTHORIZED);
		} else {
			kazanGroup.setGroupName(checkGroup.getGroupName());
			groupRepository.update(kazanGroup);
			return new ResponseEntity<String>("Group updated successfully!", HttpStatus.ACCEPTED);
		}
	}		
	
	// API 8 - last part
	@CrossOrigin
	@RequestMapping(method=RequestMethod.POST, path="/group/editrole")
	public @ResponseBody ResponseEntity<String> editRoleOfUser(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserGroupRole userGroupRole) {
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
	
}
