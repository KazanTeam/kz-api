package com.kazan.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kazan.component.TelegramSender;
import com.kazan.model.BaseObject;
import com.kazan.model.KazanGroup;
import com.kazan.model.Message;
import com.kazan.model.ObjectAlert;
import com.kazan.model.ObjectMaster;
import com.kazan.model.ObjectNormal;
import com.kazan.repository.AbstractObjectRepository;
import com.kazan.repository.GroupRepository;
import com.kazan.repository.UserGroupRoleRepository;
import com.kazan.repository.UserRepository;
import com.kazan.wrapper.AlertRequestWrapper;
import com.kazan.wrapper.AuthorizationHeaderWrapper;
import com.kazan.wrapper.NewGroupRequestWrapper;
import com.kazan.wrapper.ObjectRequestWrapper;

@RestController    
@RequestMapping(path="/kazan")
public class KazanController {
	
	@Autowired
	private AbstractObjectRepository<ObjectNormal> objectNormalRepository;
	
	@Autowired
	private AbstractObjectRepository<ObjectMaster>  objectMasterRepository;
	
	@Autowired
	private AbstractObjectRepository<ObjectAlert>  objectAlertRepository;	
	
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
		int roleId = ugrRepository.getByGroupIdUserId(userId, groupId); 
		if(checkSendMessagePermissionByRoleIdAndMode(roleId, 0)) return groupId;
		return -1;
	}
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
	
	private int synObject(String groupAliase,int userId, String symbol, int mode, List<BaseObject> objects) {
		int groupId = ugrRepository.getGroupIdByGroupAlias(userId, groupAliase);
		if (-1 == groupId) return -1;
		int roleId = ugrRepository.getByGroupIdUserIdSymbol(userId, groupId, symbol); 
		if(checkPushPermissionByRoleIdAndMode(roleId, mode)) {
			try {
				if(mode==3) {
					objectNormalRepository.deleteBySymbolUserGroup(symbol, userId, groupId);
				} else if(mode==2) {
					objectMasterRepository.deleteBySymbolGroup(symbol, groupId);
				} else if(mode==4 || mode==5) {
					objectAlertRepository.deleteBySymbolGroup(symbol, groupId);
				}
			} catch(Exception e) {
				System.out.println("KazanController.synObject:" + e);
				return -1;
			}
			if (objects != null && !objects.isEmpty()) {
				Date objectUdatedDate = new Date();
				for (BaseObject ko : objects) {
					ko.setSymbol(symbol);
					ko.setUserId(userId);
					ko.setGroupId(groupId);
					ko.setUpdated_date(objectUdatedDate);
					if(mode==3) {
						objectNormalRepository.add(new ObjectNormal(ko));	
					} else if(mode==2) {
						objectMasterRepository.add(new ObjectMaster(ko));
					} else if(mode==4 || mode==5) {
						objectAlertRepository.add(new ObjectAlert(ko));
					}
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
				if(-1 == getFromUserId) {
					String[][] userUpdate = objectNormalRepository.getUserIdAndUpdateTime(wrapperObject.getSymbol(), groupId);
					if (userUpdate.length > 0 && userUpdate[0].length > 0)
						getFromUserId = userRepository.getIdByUsername(userUpdate[0][0]);
				}
				
				if(wrapperObject.getMode()==3) {
					return new ResponseEntity<String>(mapper.writeValueAsString(objectNormalRepository.getBySymbolUserGroup(wrapperObject.getSymbol(), getFromUserId, groupId)), HttpStatus.ACCEPTED);
				} else if(wrapperObject.getMode()==2) {
					return new ResponseEntity<String>(mapper.writeValueAsString(objectMasterRepository.getBySymbolGroup(wrapperObject.getSymbol(), getFromUserId, groupId)), HttpStatus.ACCEPTED);
				} else if(wrapperObject.getMode()==4 || wrapperObject.getMode()==5) {
					return new ResponseEntity<String>(mapper.writeValueAsString(objectAlertRepository.getBySymbolGroup(wrapperObject.getSymbol(), getFromUserId, groupId)), HttpStatus.ACCEPTED);
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
		if(checkUserGetPermissionByRoleIdAndMode(roleId, mode)) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				String[][] userObjects = null;
				if (3 == mode)
					userObjects  = objectNormalRepository.getUserIdAndUpdateTime(wrapperObject.getSymbol(), groupId);
				else if (2 == mode)
					userObjects  = objectMasterRepository.getUserIdAndUpdateTime(wrapperObject.getSymbol(), groupId);
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
	boolean checkUserGetPermissionByRoleIdAndMode(int roleId, int mode) {
		if(roleId==2 || roleId==3) return true;
		return false;
	}
	

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
}
