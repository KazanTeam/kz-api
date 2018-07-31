package com.kazan.component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kazan.model.KazanGroup;
import com.kazan.model.Message;
import com.kazan.repository.GroupRepository;
import com.kazan.repository.KazanObjectRepository;
import com.kazan.repository.UserGroupRoleRepository;
import com.kazan.repository.UserRepository;

@Component
public class TelegramSendGroups {
	
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
	
	
	public void sendMessage(List<Integer> groupIds, int mode, int TelegramBotType, String content, String  note, String imageUrl) {
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
}
